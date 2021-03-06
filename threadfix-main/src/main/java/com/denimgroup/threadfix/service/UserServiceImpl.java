////////////////////////////////////////////////////////////////////////
//
//     Copyright (c) 2009-2015 Denim Group, Ltd.
//
//     The contents of this file are subject to the Mozilla Public License
//     Version 2.0 (the "License"); you may not use this file except in
//     compliance with the License. You may obtain a copy of the License at
//     http://www.mozilla.org/MPL/
//
//     Software distributed under the License is distributed on an "AS IS"
//     basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
//     License for the specific language governing rights and limitations
//     under the License.
//
//     The Original Code is ThreadFix.
//
//     The Initial Developer of the Original Code is Denim Group, Ltd.
//     Portions created by Denim Group, Ltd. are Copyright (C)
//     Denim Group, Ltd. All Rights Reserved.
//
//     Contributor(s): Denim Group, Ltd.
//
////////////////////////////////////////////////////////////////////////
package com.denimgroup.threadfix.service;

import com.denimgroup.threadfix.data.dao.AccessControlMapDao;
import com.denimgroup.threadfix.data.dao.RoleDao;
import com.denimgroup.threadfix.data.dao.UserDao;
import com.denimgroup.threadfix.data.entities.*;
import com.denimgroup.threadfix.logging.SanitizedLogger;
import com.denimgroup.threadfix.service.enterprise.EnterpriseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    protected final SanitizedLogger log = new SanitizedLogger(UserService.class);

    @Autowired
    private UserDao             userDao             = null;
    @Autowired
    private RoleDao             roleDao             = null;
    @Autowired
    private AccessControlMapDao accessControlMapDao = null;

    private ThreadFixPasswordEncoder encoder = new ThreadFixPasswordEncoder();
    private Authentication authentication;

    /**
     * Transactional(readOnly = false) here means that false will be put in to
     * the LDAP user field and update correctly.
     */
    @Override
    @Transactional(readOnly = false)
    public List<User> loadAllUsers() {
        return userDao.retrieveAllActive();
    }

    @Override
    public User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = null;

        if (principal instanceof ThreadFixUserDetails) {

            ThreadFixUserDetails details = (ThreadFixUserDetails) principal;

            Integer userId = details.getUserId();

            user = userDao.retrieveById(userId);
        }

        return user;
    }

    @Override
    @Transactional(readOnly = false) // used to be true
    public User loadUser(int userId) {
        return userDao.retrieveById(userId);
    }

    @Override
    @Transactional(readOnly = false)
    public User loadUser(String name) {
        User user = userDao.retrieveByName(name);
        if (user != null && user.getIsLdapUser()) {
            return null;
        } else {
            return user;
        }
    }

    @Override
    @Transactional(readOnly = false)
    public void storeUser(User user) {
        if ((user.getUnencryptedPassword() != null) && (user.getUnencryptedPassword().length() > 0)) {
            encryptPassword(user);
        }
        userDao.saveOrUpdate(user);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(User user) {
        if (user != null) {
            user.setName(user.getName() + new Date().toString());
            if (user.getName().length() > User.NAME_LENGTH) {
                user.setName(user.getName().substring(0, User.NAME_LENGTH - 1));
            }

            user.setActive(false);
            userDao.saveOrUpdate(user);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public Integer createUser(User user) {
        encryptPassword(user);
        userDao.saveOrUpdate(user);
        return user.getId();
    }

    private void encryptPassword(User user) {
        try {
            user.setSalt(encoder.generateSalt());
            user.setPassword(encoder.generatePasswordHash(user.getUnencryptedPassword(),
                    user.getSalt()));
        } catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	@Override
	@Transactional(readOnly = false) // used to be true
	public boolean isCorrectPassword(User user, String password) {
		if (user.getPassword() != null && user.getSalt() != null 
				&& password != null) {
			try {
				String encodedPassword = encoder.generatePasswordHash(password, user.getSalt());
				return encodedPassword != null && encodedPassword.equals(user.getPassword());
			} catch (NoSuchAlgorithmException e) {
				// This should never happen but let's log it
				log.warn("Failed to encrypt a password - something is broken.", e);
			}
		} 

		return false;
	}

	@Override
	@Transactional(readOnly = false) // used to be true
	public Set<Permission> getGlobalPermissions(Integer userId) {
		Set<Permission> returnList = new HashSet<>();

		// for now
		User user = loadUser(userId);
		
		if (user != null && user.getHasGlobalGroupAccess() && user.getGlobalRole() != null) {
            user.getGlobalRole().setEnterprise(EnterpriseTest.isEnterprise());
			returnList.addAll(user.getGlobalRole().getPermissions());
		}
		
		return returnList;
	}

	@Override
	@Transactional(readOnly = false) // used to be true
	public boolean canDelete(User user) {
		boolean canDelete = true;
		
		Set<Permission> permissions = getGlobalPermissions(user.getId());

		if (permissions.contains(Permission.CAN_MANAGE_USERS) &&
				!userDao.canRemovePermissionFromUser(user.getId(), "canManageUsers")) {
			canDelete = false;
		}

		if (canDelete && permissions.contains(Permission.CAN_MANAGE_ROLES) && 
				!userDao.canRemovePermissionFromUser(user.getId(), "canManageRoles")) {
			canDelete = false;
		}
		
		return canDelete;
	}
	
	@Override
	@Transactional(readOnly = false) // used to be true
	public boolean canSetRoles(int userId, List<Integer> objectIds) {
		boolean canSetRoles = true;
		
		Set<Permission> oldPermissions = getGlobalPermissions(userId);
		Set<Permission> newPermissions = new HashSet<>();
		
		if (objectIds != null) {
			for (Integer integer : objectIds) {
				Role role = roleDao.retrieveById(integer);
				
				if (role != null) {
					newPermissions.addAll(role.getPermissions());
				}
			}
		}
		
		if (oldPermissions.contains(Permission.CAN_MANAGE_USERS) &&
				!newPermissions.contains(Permission.CAN_MANAGE_USERS) &&
				!userDao.canRemovePermissionFromUser(userId, "canManageUsers")) {
			canSetRoles = false;
		}
		
		if (canSetRoles && oldPermissions.contains(Permission.CAN_MANAGE_ROLES) &&
				!newPermissions.contains(Permission.CAN_MANAGE_ROLES) &&
				!userDao.canRemovePermissionFromUser(userId, "canManageRoles")) {
			canSetRoles = false;
		}
		
		return canSetRoles;
	}

	@Override
	@Transactional(readOnly = false) // used to be true
	public Map<Integer, Set<Permission>> getApplicationPermissions(
			Integer userId) {
		
		Map<Integer, Set<Permission>> applicationPermissions = new HashMap<>();
		List<AccessControlTeamMap> maps = accessControlMapDao.retrieveAllMapsForUser(userId);
		
		if (maps != null) {
			for (AccessControlTeamMap teamMap : maps) {
				if (teamMap != null && teamMap.getAccessControlApplicationMaps() != null) {
					for (AccessControlApplicationMap appMap : teamMap.getAccessControlApplicationMaps()) {
						if (appMap != null && appMap.getApplication() != null && 
								appMap.getApplication().getId() != null && 
								appMap.getRole() != null && 
								appMap.getRole().getPermissions() != null) {
							applicationPermissions.put(appMap.getApplication().getId(), 
									appMap.getRole().getPermissions());
							applicationPermissions.get(appMap.getApplication().getId()).add(Permission.READ_ACCESS);
						}
					}
				}
			}
		}
		
		return applicationPermissions;
	}

	@Override
	@Transactional(readOnly = false) // used to be true
	public Map<Integer, Set<Permission>> getOrganizationPermissions(
			Integer userId) {
		Map<Integer, Set<Permission>> organizationPermissions = new HashMap<>();
		List<AccessControlTeamMap> maps = accessControlMapDao.retrieveAllMapsForUser(userId);
		
		if (maps != null) {
			for (AccessControlTeamMap map : maps) {
				if (map != null && map.getOrganization() != null && 
						map.getOrganization().getId() != null && 
						map.getRole() != null && 
						map.getRole().getPermissions() != null) {
					organizationPermissions.put(map.getOrganization().getId(), 
							map.getRole().getPermissions());
					organizationPermissions.get(map.getOrganization().getId()).add(Permission.READ_ACCESS);
				}
			}
		}
		
		return organizationPermissions;
	}
	
	@Override
	@Transactional(readOnly = false) // used to be true
	public boolean hasRemovedAdminPermissions(User user) {
		
		if (user == null || user.getId() == null) {
			return true; // should never get here
		}
		
		Set<Permission> dbPerms = getGlobalPermissions(user.getId());
		
		if (user.getGlobalRole() == null || user.getGlobalRole().getId() == null) {
		 return dbPerms.contains(Permission.CAN_MANAGE_USERS) || 
				dbPerms.contains(Permission.CAN_MANAGE_ROLES);
		}
		
		Role newRole = roleDao.retrieveById(user.getGlobalRole().getId());
		
		if (newRole == null) {
			return false;
		}
		
		Set<Permission> newPerms = newRole.getPermissions();
		
		if (newPerms == null) {
			return false;
		}
		
		return user.getGlobalRole() != null && 
				(!newPerms.contains(Permission.CAN_MANAGE_USERS) && dbPerms.contains(Permission.CAN_MANAGE_USERS)) ||
				(!newPerms.contains(Permission.CAN_MANAGE_ROLES) && dbPerms.contains(Permission.CAN_MANAGE_ROLES));
	}

	@Override
	@Transactional(readOnly = false) // used to be true
	public User loadLdapUser(String name) {
		return userDao.retrieveLdapUser(name);
	}
	
	
	// This is a terrible idea, we should switch to a strategy that 
	// actually lets us use normal model validation
	@Override
	public User applyChanges(User user, Integer userId) {
		if (user == null || userId == null) {
			return null;
		}
		
		User returnUser = loadUser(userId);
		if (returnUser == null) {
			return null;
		}
		
//		returnUser.setName(user.getName());
//		returnUser.setGlobalRole(user.getGlobalRole());
//		returnUser.setUnencryptedPassword(user.getUnencryptedPassword());
//		returnUser.setPasswordConfirm(user.getPasswordConfirm());
//		returnUser.setHasGlobalGroupAccess(user.getHasGlobalGroupAccess());
//		returnUser.setIsLdapUser(user.getIsLdapUser());
		
		user.setAccessControlTeamMaps(returnUser.getAccessControlTeamMaps());
		user.setActive(returnUser.isActive());
		user.setApproved(returnUser.isApproved());
		user.setCreatedDate(returnUser.getCreatedDate());
		user.setCurrentPassword(returnUser.getCurrentPassword());
		user.setFailedPasswordAttempts(returnUser.getFailedPasswordAttempts());
		user.setFailedPasswordAttemptWindowStart(returnUser.getFailedPasswordAttemptWindowStart());
		user.setHasChangedInitialPassword(returnUser.isHasChangedInitialPassword());
		user.setId(userId);
		user.setLastLoginDate(returnUser.getLastLoginDate());
		user.setLastPasswordChangedDate(returnUser.getLastPasswordChangedDate());
		user.setModifiedDate(returnUser.getModifiedDate());
		user.setSalt(returnUser.getSalt());
		user.setPassword(returnUser.getPassword());
		user.setWasLdapUser(returnUser.getIsLdapUser());
		
		return user;
	}

	@Override
	public List<User> getPermissibleUsers(Integer orgId, Integer appId) {	
		List<User> resultList = null;
		if (orgId != null && appId == null) resultList = userDao.retrieveOrgPermissibleUsers(orgId);			
		if (appId != null && orgId != null) resultList = userDao.retrieveAppPermissibleUsers(orgId, appId);			
		return resultList;
	}

    @Override
    public boolean shouldReloadUserIfRoleChanged(Role role) {

        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        if (name == null) {
            assert false;
            return true;
        }

        User user = loadUser(name);

        if (user == null) {
            assert false;
            return true;
        }

        if (user.getGlobalRole() != null && user.getGlobalRole().getId().equals(role.getId())) {
            return true;
        }

        for (AccessControlTeamMap map : user.getAccessControlTeamMaps()) {
            if (map.isActive() && map.getRole().getId().equals(role.getId())) {
                return true;
            }

            for (AccessControlApplicationMap applicationMapping : map.getAccessControlApplicationMaps()) {
                if (applicationMapping.isActive() && applicationMapping.getRole().getId().equals(role.getId())) {
                    return true;
                }
            }
        }

        return false;
    }

	@Override
	public void setRoleCommunity(User user) {

		if (!EnterpriseTest.isEnterprise()) {
			Role administrator = roleDao.retrieveByName("Administrator");
			if (administrator == null) {
				log.error("Administrator role not found in community version. Check your database");
			} else {
				user.setGlobalRole(administrator);
				user.setHasGlobalGroupAccess(true);
			}
		}
	}

	@Override
	public List<User> retrievePage(int page, int numberToShow) {
		return userDao.retrievePage(page, numberToShow);
	}

	@Override
	public Long countUsers() {
		return userDao.countUsers();
	}

}
