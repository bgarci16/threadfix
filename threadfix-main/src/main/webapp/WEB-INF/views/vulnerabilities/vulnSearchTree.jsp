<div class="vuln-tree">
    <h3>
        <span style="float:left">
            Results
        </span>
        <span class="spinner-div">
            <span ng-show="loadingTree" class="spinner dark"></span>
        </span>
    </h3>

    <div ng-hide="!vulnTree || vulnTree.length > 0">
        No results found.
    </div>

    <table ng-show="vulnTree">
        <tbody ng-repeat="category in vulnTree" ng-show="category.total > 0">
            <tr>
                <td>
                    <span ng-hide="category.expanded" ng-click="category.expanded = true" class="icon icon-plus-sign"></span>
                    <span ng-show="category.expanded" ng-click="category.expanded = false" class="icon icon-minus-sign"></span>
                </td>
                <td style="width:50px">
                    {{ category.name }}
                </td>
                <td ng-style="badgeWidth">
                    <span ng-style="badgeWidth" class="badge" ng-class="{
                                            'badge-important': category.intValue === 5,
                                            'badge-warning': category.intValue === 4,
                                            'badge-success': category.intValue === 3,
                                            'badge-info': category.intValue === 2 || element.intValue === 1
                                            }">
                        {{ category.total }}
                    </span>
                </td>
            </tr>
            <tr ng-repeat-start="element in category.entries" ng-show="category.expanded">
                <td></td>
                <td>
                    <span ng-hide="element.expanded" ng-click="expandAndRetrieveTable(element)" class="icon icon-plus-sign"></span>
                    <span ng-show="element.expanded" ng-click="element.expanded = false" class="icon icon-minus-sign"></span>
                </td>
                <td>
                    <span ng-style="badgeWidth" class="badge" ng-class="{
                                    'badge-important': element.intValue === 5,
                                    'badge-warning': element.intValue === 4,
                                    'badge-success': element.intValue === 3,
                                    'badge-info': element.intValue === 2 || element.intValue === 1
                                    }">
                        {{ element.numResults }}
                    </span>
                </td>
                <td>
                    {{ element.genericVulnerability.name | shortCweNames }}
                </td>
            </tr>
            <tr ng-show="category.expanded && element.expanded" ng-repeat-end>
                <td></td>
                <td colspan="4" class="vuln-tree-vuln-list">
                    <div class="accordion-group">
                        <%@ include file="vulnTreeGroupHeader.jsp" %>
                        <%@ include file="vulnTreeVulnRows.jsp" %>
                    </div>
                </td>
            </tr>
        </tbody>
    </table>
</div>