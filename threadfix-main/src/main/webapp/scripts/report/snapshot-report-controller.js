var module = angular.module('threadfix');

module.controller('SnapshotReportController', function($scope, $rootScope, $window, $http, tfEncoder, vulnSearchParameterService,
                                                       vulnTreeTransformer, reportUtilities, reportExporter) {

    $scope.parameters = {};
    $scope.noData = false;
    $scope.hideTitle = true;
    $scope.margin = {top: 85, right: 100, bottom: 70, left: 70};
    $scope.PIT_Report_Id = 2;
    $scope.PBV_Report_Id = 3;
    $scope.MVA_Report_Id = 10;
    $scope.OWASP_Report_Id = 11;
    $scope.Portfolio_Report_Id = 12;

    $scope.snapshotOptions = [
        { name: "Point in Time", id: $scope.PIT_Report_Id },
        { name: "Progress By Vulnerability", id: $scope.PBV_Report_Id },
        { name: "Most Vulnerable Applications", id: $scope.MVA_Report_Id },
        { name: "OWASP Top 10", id: $scope.OWASP_Report_Id },
        { name: "Portfolio", id: $scope.Portfolio_Report_Id }
    ];

    $scope.graphIdMap = {
    };
    $scope.graphIdMap[$scope.PIT_Report_Id] = "pointInTimeGraph";
    $scope.graphIdMap[$scope.MVA_Report_Id] = "mostVulnAppsGraph";

    $scope.htmlElementIdMap = {
    };
    $scope.htmlElementIdMap[$scope.PIT_Report_Id] = "pointInTimeTablePdf";
    $scope.htmlElementIdMap[$scope.OWASP_Report_Id] = "pointInTimeTablePdf";
    $scope.htmlElementIdMap[$scope.PBV_Report_Id] = "progressVulnsDiv";
    $scope.htmlElementIdMap[$scope.Portfolio_Report_Id] = "portfolioDiv";

    $scope.titleMap = {};
    $scope.titleMap[$scope.PIT_Report_Id] = "Point_In_Time";
    $scope.titleMap[$scope.OWASP_Report_Id] = "OWASP_Top_10";
    $scope.titleMap[$scope.PBV_Report_Id] = "Progress_By_Vulnerability";
    $scope.titleMap[$scope.MVA_Report_Id] = "Most_Vulnerable_Applications";
    $scope.titleMap[$scope.Portfolio_Report_Id] = "Portfolio";


    $scope.OWASP_TOP10 = [
        {
            year: 2013,
            top10: [
                {
                    id: 'A1',
                    name: 'A1 - Injection',
                    members: [77, 78, 88, 89, 90, 91, 929]
                },
                {
                    id: 'A2',
                    name: "A2 - Broken Authentication and Session Management",
                    members: [256, 287, 311, 319, 384, 522, 523, 613, 620, 640, 930]
                },
                {
                    id: 'A3',
                    name: "A3 - Cross-Site Scripting (XSS)",
                    members: [79, 931]
                },
                {
                    id: 'A4',
                    name: "A4 - Insecure Direct Object References",
                    members: [22, 99, 639, 932]
                },
                {
                    id: 'A5',
                    name: "A5 - Security Misconfiguration",
                    members: [2, 16, 209, 215, 548, 933]
                },
                {
                    id: 'A6',
                    name: "A6 - Sensitive Data Exposure",
                    members: [310, 311, 312, 319, 320, 325, 326, 327, 328, 934]
                },
                {
                    id: 'A7',
                    name: "A7 - Missing Function Level Access Control",
                    members: [285, 287, 935]
                },
                {
                    id: 'A8',
                    name: "A8 - Cross-Site Request Forgery (CSRF)",
                    members: [352, 936]
                },
                {
                    id: 'A9',
                    name: "A9 - Using Components with Known Vulnerabilities",
                    members: [937]
                },
                {
                    id: 'A10',
                    name: "A10 - Unvalidated Redirects and Forwards",
                    members: [601, 938]
                }
            ]
        },
        {
            year: 2010,
            top10: [
                {
                    id: 'A1',
                    name: 'A1 - Injection',
                    members: [78, 88, 89, 90, 91, 810]
                },
                {
                    id: 'A2',
                    name: "A2 - Cross-Site Scripting (XSS)",
                    members: [79, 811]

                },
                {
                    id: 'A3',
                    name: "A3 - Broken Authentication and Session Management",
                    members: [287, 306, 307, 798, 812]
                },
                {
                    id: 'A4',
                    name: "A4 - Insecure Direct Object References",
                    members: [22, 99, 434, 639, 829, 862, 863, 813]
                },
                {
                    id: 'A5',
                    name: "A5 - Cross-Site Request Forgery(CSRF)",
                    members: [352, 814]
                },
                {
                    id: 'A6',
                    name: "A6 - Security Misconfiguration",
                    members: [209, 219, 250, 538, 552, 732, 815]
                },
                {
                    id: 'A7',
                    name: "A7 - Insecure Cryptographic Storage",
                    members: [311, 312, 326, 327, 759, 816]
                },
                {
                    id: 'A8',
                    name: "A8 - Failure to Restrict URL Access",
                    members: [285, 862, 863, 817]
                },
                {
                    id: 'A9',
                    name: "A9 - Insufficient Transport Layer Protection",
                    members: [311, 319, 818]
                },
                {
                    id: 'A10',
                    name: "A10 - Unvalidated Redirects and Forwards",
                    members: [601, 819]
                }
            ]
        },
        {
            year: 2007,
            top10: [
                {
                    id: 'A1',
                    name: 'A1 - Cross Site Scripting (XSS)',
                    members: [79, 712]
                },
                {
                    id: 'A2',
                    name: "A2 - Injection Flaws",
                    members: [77, 89, 90, 91, 93, 713]
                },
                {
                    id: 'A3',
                    name: "A3 - Malicious File Execution",
                    members: [78, 95, 98, 434, 714]
                },
                {
                    id: 'A4',
                    name: "A4 - Insecure Direct Object Reference",
                    members: [22, 472, 639, 715]
                },
                {
                    id: 'A5',
                    name: "A5 - Cross Site Request Forgery (CSRF)",
                    members: [352, 716]
                },
                {
                    id: 'A6',
                    name: "A6 - Information Leakage and Improper Error Handling",
                    members: [200, 203, 209, 215, 717]
                },
                {
                    id: 'A7',
                    name: "A7 - Broken Authentication and Session Management",
                    members: [287, 301, 522, 718]
                },
                {
                    id: 'A8',
                    name: "A8 - Insecure Cryptographic Storage",
                    members: [311, 321, 325, 326, 719]
                },
                {
                    id: 'A9',
                    name: "A9 - Insecure Communications",
                    members: [311, 321, 325, 326, 720]
                },
                {
                    id: 'A10',
                    name: "A10 - Failure to Restrict URL Access",
                    members: [285, 288, 425, 721]
                }
            ]
        }];

    $scope.resetFilters = function() {
        $scope.parameters = {
            tags: [],
            teams: [],
            applications: [],
            scanners: [],
            genericVulnerabilities: [],
            severities: {
                info: true,
                low: true,
                medium: true,
                high: true,
                critical: true
            },
            numberVulnerabilities: 10,
            showOpen: true,
            showClosed: false,
            showFalsePositive: false,
            showHidden: false,
            showDefectPresent: false,
            showDefectNotPresent: false,
            showDefectOpen: false,
            showDefectClosed: false,
            endDate: undefined,
            startDate: undefined,
            selectedOwasp: $scope.OWASP_TOP10[0]
        };
    };

    $scope.$on('loadSnapshotReport', function() {

        $scope.noData = false;

        $scope.savedFilters = $scope.$parent.savedFilters.filter(function(filter){
            var parameters = JSON.parse(filter.json);
            return (parameters.filterType && parameters.filterType.isSnapshotFilter);
        });

        if (!$scope.allVulns) {
            $scope.loading = true;
            $scope.reportId = ($scope.$parent.reportId && $scope.$parent.reportId !== 9) ? $scope.$parent.reportId : $scope.PIT_Report_Id;
            $http.post(tfEncoder.encode("/reports/snapshot"), $scope.getReportParameters()).
                success(function(data) {
                    $scope.loading = false;
                    $scope.resetFilters();
                    $scope.allVulns = data.object.vulnList;
                    $scope.allPortfolioApps = data.object.appList;

                    $scope.tags = data.object.tags;

                    $scope.appTagMatrix = [];
                    $scope.allPortfolioApps.forEach(function(app) {
                        var tagMatrix = $scope.appTagMatrix[app.appId];
                        if (typeof tagMatrix == "undefined") {
                            tagMatrix = [];
                            $scope.appTagMatrix[app.appId] = tagMatrix;
                        }
                        app.tags.forEach(function(tag) {
                            tagMatrix[tag.name] = true;
                        });
                    });

                    if ($scope.$parent.teamId !== -1 && $scope.$parent.applicationId === -1) {
                        $scope.parameters.teams = [$scope.$parent.team];
                        $scope.parameters.applications = [];
                        $scope.$broadcast("updateBackParameters", $scope.parameters);
                    }
                    if ($scope.$parent.applicationId !== -1) {
                        var app = angular.copy($scope.$parent.application);
                        app.name = $scope.$parent.team.name + " / " + app.name;
                        $scope.parameters.applications = [app];
                        $scope.parameters.teams = [];
                        $scope.$broadcast("updateBackParameters", $scope.parameters);
                    }

                    if ($scope.allVulns) {
                        // Point In Time is default report for Snapshot
                        $scope.allPointInTimeVulns = $scope.allVulns.filter(function(vuln){
                            return vuln.active;
                        });
                        refresh();
                    } else {
                        $scope.noData = true;
                    };
                })
                .error(function() {
                    $scope.loading = false;
                });
        }

        $scope.$parent.snapshotActive = true;
        $scope.$parent.complianceActive = false;

    });

    $scope.$on('resetParameters', function(event, parameters) {
        if (!$scope.$parent.snapshotActive)
            return;
        $scope.parameters = angular.copy(parameters);
        refresh();
    });

    $scope.$on('updateDisplayData', function(event, parameters) {
        if (!$scope.$parent.snapshotActive)
            return;
        $scope.parameters = angular.copy(parameters);
        if ($scope.reportId === $scope.PIT_Report_Id) {
            filterPITBySeverity();
            updateTree();
        } else if ($scope.reportId === $scope.PBV_Report_Id) {
            $scope.filterVulns = filterByTag(filterByTeamAndApp($scope.allCWEvulns));
            filterPBVBySeverity($scope.filterVulns);
            processPBVData($scope.filterVulns);
        } else if ($scope.reportId === $scope.MVA_Report_Id) {
            filterMVABySeverity();
        } else if ($scope.reportId === $scope.OWASP_Report_Id) {
            processOWASPData();
        }
    });

    var updateTree = function () {
        var _parameters = angular.copy($scope.parameters);
        $scope.hideTitle = false;
        vulnSearchParameterService.updateParameters($scope, $scope.parameters);

        if (!$scope.title)
            $scope.title = {};
        $scope.title.tagsList = $scope.parameters.tags;
        $scope.title.teamsList = $scope.parameters.teams;
        $scope.title.appsList = $scope.parameters.applications;

        $scope.$broadcast("refreshVulnSearchTree", $scope.parameters);
        $scope.parameters = _parameters;
    }

    $scope.loadReport = function() {
        $scope.reportId = parseInt($scope.reportId);

        if ($scope.reportId !== $scope.OWASP_Report_Id) {
            $scope.parameters.startDate = undefined;
            $scope.parameters.endDate = undefined;
        }

        if ($scope.reportId === $scope.PBV_Report_Id) {
            if (!$scope.allCWEvulns) {
                $scope.allCWEvulns = $scope.allVulns.filter(function (vuln) {
                    if (!vuln.genericVulnName || vuln.isFalsePositive || vuln.hidden)
                        return false;
                    else
                        return true;
                });

                if (!$scope.allCWEvulns) {
                    $scope.noData = true;
                } else {
                    $scope.filterVulns = filterByTag(filterByTeamAndApp($scope.allCWEvulns));
                    filterPBVBySeverity($scope.filterVulns);
                    processPBVData($scope.filterVulns);
                }
            } else {
                $scope.filterVulns = filterByTag(filterByTeamAndApp($scope.allCWEvulns));
                filterPBVBySeverity($scope.filterVulns);
                processPBVData($scope.filterVulns);
            }
        } else if ($scope.reportId === $scope.PIT_Report_Id) {

            if (!$scope.allPointInTimeVulns) {
                $scope.noData = true;
            } else {
                $scope.filterVulns = filterByTag(filterByTeamAndApp($scope.allPointInTimeVulns));
                updateTree();
                processPITData();
                filterPITBySeverity();
            }
        } else if ($scope.reportId === $scope.MVA_Report_Id) {
            processMVAData();
        } else if ($scope.reportId === $scope.OWASP_Report_Id) {
            processOWASPData();
        } else if ($scope.reportId === $scope.Portfolio_Report_Id) {
            $scope.filterPortfolioApps = filterByTag(filterByTeamAndApp($scope.allPortfolioApps));
            processPortfolioData();
        }
    }

    var processPBVData = function(allCWEvulns) {
        $scope.progressByTypeData = [];
        var statsMap = {};
        var now = (new Date()).getTime();

        allCWEvulns.forEach(function(vuln){
            var key = vuln.genericVulnName;
            if (!statsMap[key]) {
                statsMap[key] = {
                    numOpen : 0,
                    numClosed : 0,
                    totalAgeOpen : 0,
                    totalTimeToClose : 0
                }
            }

            if (vuln.active) {
                statsMap[key]["numOpen"] = statsMap[key]["numOpen"] + 1;
                statsMap[key]["totalAgeOpen"] = statsMap[key]["totalAgeOpen"] + getDates(now, vuln.importTime);
            } else {
                statsMap[key]["numClosed"] = statsMap[key]["numClosed"] + 1;

                //If there is no information about Close Time, then will default Close Time is Today
                statsMap[key]["totalTimeToClose"] = statsMap[key]["totalTimeToClose"] + getDates((vuln.closeTime) ? vuln.closeTime : now, vuln.importTime);
            }
        });

        var keys = Object.keys(statsMap);
        keys.forEach(function(key){
            var mapEntry = statsMap[key];
            var genericVulnEntry = {
                total : mapEntry["numOpen"] + mapEntry["numClosed"],
                description : key
            };

            genericVulnEntry.percentClosed = (genericVulnEntry.total === 0) ? 100 : getPercentNumber(mapEntry["numClosed"]/genericVulnEntry.total);
            genericVulnEntry.averageAgeOpen = (mapEntry["numOpen"] === 0) ? 0 : Math.round(mapEntry["totalAgeOpen"]/mapEntry["numOpen"]);
            genericVulnEntry.averageTimeToClose = (mapEntry["numClosed"] === 0) ? 0 : Math.round(mapEntry["totalTimeToClose"]/mapEntry["numClosed"]);

            $scope.progressByTypeData.push(genericVulnEntry);
        });

        // Sorting by Total is default
        $scope.$parent.setSortNumber($scope.progressByTypeData, "total");
    };

    var filterPBVBySeverity = function(allVulns) {
        $scope.filterVulns = allVulns.filter(function(vuln){
            if ("Critical" === vuln.severity) {
                return $scope.parameters.severities.critical;
            } else if ("High" === vuln.severity) {
                return $scope.parameters.severities.high;
            } else if ("Medium" === vuln.severity) {
                return $scope.parameters.severities.medium;
            } else if ("Low" === vuln.severity) {
                return $scope.parameters.severities.low;
            } else if ("Info" === vuln.severity) {
                return $scope.parameters.severities.info;
            }
            return false;
        });
    }

    var refresh = function(){
        if ($scope.reportId === $scope.PIT_Report_Id) {
            $scope.filterVulns = filterByTag(filterByTeamAndApp($scope.allPointInTimeVulns));
            updateTree();
        } else if ($scope.reportId === $scope.PBV_Report_Id) {
            $scope.filterVulns = filterByTag(filterByTeamAndApp($scope.allCWEvulns));
        } else if ($scope.reportId === $scope.MVA_Report_Id) {
            processMVAData();
            reportUtilities.createTeamAppNames($scope);
        } else if ($scope.reportId === $scope.OWASP_Report_Id) {
            processOWASPData();
        } else if ($scope.reportId === $scope.Portfolio_Report_Id) {
            $scope.filterPortfolioApps = filterByTag(filterByTeamAndApp($scope.allPortfolioApps));
        }

        if ($scope.filterVulns.length === 0) {
            $scope.noData = true;
        } else {
            $scope.noData = false;
        }
        updateDisplayData();
    };

    var updateDisplayData = function(){
        reportUtilities.createTeamAppNames($scope);
        if ($scope.reportId === $scope.PIT_Report_Id) {
            processPITData();
            filterPITBySeverity();
        } else if ($scope.reportId === $scope.PBV_Report_Id) {
            filterPBVBySeverity($scope.filterVulns);
            processPBVData($scope.filterVulns);
        }  else if ($scope.reportId === $scope.Portfolio_Report_Id) {
            processPortfolioData();
        }
    };

    var processPITData = function() {
        $scope.data = {
            Critical: {
                Severity: 'Critical',
                Count: 0,
                Avg_Age: 0,
                Percentage: '0%'
            },
            High: {
                Severity: 'High',
                Count: 0,
                Avg_Age: 0,
                Percentage: '0%'
            },
            Medium: {
                Severity: 'Medium',
                Count: 0,
                Avg_Age: 0,
                Percentage: '0%'
            },
            Low: {
                Severity: 'Low',
                Count: 0,
                Avg_Age: 0,
                Percentage: '0%'
            },
            Info: {
                Severity: 'Info',
                Count: 0,
                Avg_Age: 0,
                Percentage: '0%'
            }
        };

        var highAgeSum = 0,
            mediumAgeSum = 0,
            criticalAgeSum = 0,
            lowAgeSum = 0,
            infoAgeSum = 0,
            totalCount;

        var now = (new Date()).getTime();

        $scope.filterVulns.forEach(function(vuln){
            if ("High" === vuln.severity) {
                $scope.data.High.Count += 1;
                highAgeSum += getDates(now, vuln.importTime);
            } else if ("Medium" === vuln.severity) {
                $scope.data.Medium.Count += 1;
                mediumAgeSum += getDates(now, vuln.importTime);
            } else if ("Critical" === vuln.severity) {
                $scope.data.Critical.Count += 1;
                criticalAgeSum += getDates(now, vuln.importTime);
            } else if ("Low" === vuln.severity) {
                $scope.data.Low.Count += 1;
                lowAgeSum += getDates(now, vuln.importTime);
            } else if ("Info" === vuln.severity) {
                $scope.data.Info.Count += 1;
                infoAgeSum += getDates(now, vuln.importTime);
            }
        });

        totalCount = $scope.data.High.Count + $scope.data.Medium.Count + $scope.data.Critical.Count + $scope.data.Low.Count + $scope.data.Info.Count;

        if (totalCount !== 0) {
            $scope.data.Critical.Percentage = getPercent($scope.data.Critical.Count/totalCount);
            $scope.data.High.Percentage = getPercent($scope.data.High.Count/totalCount);
            $scope.data.Medium.Percentage = getPercent($scope.data.Medium.Count/totalCount);
            $scope.data.Low.Percentage = getPercent($scope.data.Low.Count/totalCount);
            $scope.data.Info.Percentage = getPercent($scope.data.Info.Count/totalCount);
        }

        $scope.data.High.Avg_Age = ($scope.data.High.Count !== 0) ? Math.round(highAgeSum/$scope.data.High.Count) : 0;
        $scope.data.Critical.Avg_Age = ($scope.data.Critical.Count !== 0) ? Math.round(criticalAgeSum/$scope.data.Critical.Count) : 0;
        $scope.data.Medium.Avg_Age = ($scope.data.Medium.Count !== 0) ? Math.round(mediumAgeSum/$scope.data.Medium.Count) : 0;
        $scope.data.Low.Avg_Age = ($scope.data.Low.Count !== 0) ? Math.round(lowAgeSum/$scope.data.Low.Count) : 0;
        $scope.data.Info.Avg_Age = ($scope.data.Info.Count !== 0) ? Math.round(infoAgeSum/$scope.data.Info.Count) : 0;

    };

    var filterPITBySeverity = function() {
        var criticalCount = $scope.parameters.severities.critical ? $scope.data.Critical.Count : 0;
        var highCount = $scope.parameters.severities.high ? $scope.data.High.Count : 0;
        var mediumCount = $scope.parameters.severities.medium ? $scope.data.Medium.Count : 0;
        var lowCount = $scope.parameters.severities.low ? $scope.data.Low.Count : 0;
        var infoCount = $scope.parameters.severities.info ? $scope.data.Info.Count : 0;

        var totalCount = criticalCount + highCount + mediumCount + lowCount + infoCount;

        if (totalCount !== 0) {
            $scope.data.Critical.Percentage = getPercent($scope.data.Critical.Count/totalCount);
            $scope.data.High.Percentage = getPercent($scope.data.High.Count/totalCount);
            $scope.data.Medium.Percentage = getPercent($scope.data.Medium.Count/totalCount);
            $scope.data.Low.Percentage = getPercent($scope.data.Low.Count/totalCount);
            $scope.data.Info.Percentage = getPercent($scope.data.Info.Count/totalCount);
        }

        $scope.pointInTimeData = {};
        if ($scope.parameters.severities.critical)
            $scope.pointInTimeData.Critical = $scope.data.Critical;
        if ($scope.parameters.severities.high)
            $scope.pointInTimeData.High = $scope.data.High;
        if ($scope.parameters.severities.medium)
            $scope.pointInTimeData.Medium = $scope.data.Medium;
        if ($scope.parameters.severities.low)
            $scope.pointInTimeData.Low = $scope.data.Low;
        if ($scope.parameters.severities.info)
            $scope.pointInTimeData.Info = $scope.data.Info;

    }

    var getDates = function(firstTime, secondTime) {
        return Math.round((firstTime - secondTime) / (1000 * 3600 * 24));
    };

    var getPercent = function(rate) {
        return getPercentNumber(rate) + "%";
    }

    var getPercentNumber = function(rate) {
        return Math.round(1000 * rate)/10;
    }

    var filterByTeamAndApp = function(rawList) {

        var filteredList;

        if ($scope.parameters.teams.length === 0
            && $scope.parameters.applications.length === 0)
            filteredList = rawList;
        else {
            filteredList = rawList.filter(function(vuln){
                var i;
                for (i=0; i<$scope.parameters.teams.length; i++) {
                    if (vuln.teamName === $scope.parameters.teams[i].name) {
                        return true;
                    }
                }

                for (i=0; i<$scope.parameters.applications.length; i++) {
                    if (beginsWith($scope.parameters.applications[i].name, vuln.teamName + " / ") &&
                        endsWith($scope.parameters.applications[i].name, " / " + vuln.appName)) {
                        return true;
                    }
                }
                return false;
            });
        }
        return filteredList;
    };

    var filterByTag = function(rawList) {

        var filteredList;

        if ($scope.parameters.tags.length === 0)
            filteredList = rawList;
        else {
            filteredList = rawList.filter(function(vuln){
                var i;
                for (i=0; i<$scope.parameters.tags.length; i++) {
                    if ($scope.appTagMatrix[vuln.appId][$scope.parameters.tags[i].name]) {
                        return true;
                    }
                }
                return false;
            });
        }
        return filteredList;
    };

    var processMVAData = function() {

        $scope.loading = true;

        var parameters = angular.copy($scope.parameters);

        vulnSearchParameterService.updateParameters($scope.$parent, parameters);

        $http.post(tfEncoder.encode("/reports/getTopApps"), parameters).
            success(function(data) {

                $scope.topAppsData = data.object.appList;
                if ($scope.topAppsData) {
                    $scope._topAppsData = angular.copy($scope.topAppsData);
                    filterMVABySeverity();

                } else {
                    $scope.noData = true;
                };
                $scope.loading = false;
            })
            .error(function() {
                $scope.loading = false;
            });

    };

    var processOWASPData = function() {

        $scope.loading = true;

        var parameters = angular.copy($scope.parameters);
        parameters.owasp = parameters.selectedOwasp;
        vulnSearchParameterService.updateParameters($scope, parameters);

        $scope.$broadcast("refreshVulnSearchTree", parameters);
        $scope.loading = false;


    };

    var appCriticalityMap = {"Critical" : 0, "High" : 1, "Medium" : 2, "Low" : 3};
    var appMonthsMap = {"Criticality": 0, "1 Month" : 1, "2 Months" : 2, "3 Months" : 3, "6 Months" : 4, "9 Months": 5, "12 Months" : 6, "12+ Months" : 7, "Never": 8, "Total" : 9};

    var processPortfolioData = function() {
        var now = new Date(), latestScanTime, temp, monthsOld, index;
        if (!$scope.filterPortfolioApps) return;
        var critical = {'criticality' : 'Critical'}, high = {'criticality' : 'High'}, medium = {'criticality' : 'Medium'}, low = {'criticality' : 'Low'};
        $scope.appsByCriticality = [critical, high, medium, low];
        $scope.filterPortfolioApps.forEach(function(app){
            temp = $scope.appsByCriticality[appCriticalityMap[app.criticality]];
            temp['Total'] = (temp['Total'] ? temp['Total'] + 1 : 1);
            if (!app.noOfScans || !app.latestScanTime) {
                temp['Never'] = (temp['Never'] ? temp['Never'] + 1 : 1);
            } else {
                latestScanTime = new Date(app.latestScanTime);
                monthsOld = (now.getFullYear() - latestScanTime.getFullYear()) * 12 + now.getMonth() - latestScanTime.getMonth();
                if (monthsOld < 2)
                    index = "1Month";
                else if (monthsOld < 4)
                    index = "3Months";
                else if (monthsOld < 7)
                    index = "6Months";
                else if (monthsOld < 13)
                    index = "12Months";
                else index = 'Years';

                temp[index] = (temp[index] ? temp[index] + 1 : 1);
            }
        });

        $scope.appsByCriticality.forEach(function(appRow){
            Object.keys(appRow).forEach(function(key){
                if (key !== 'criticality' && key !== 'Total') {
                    if (!appRow[key])
                        appRow[key] = 0;
                    else {
                        appRow[key] = appRow[key] + "(" + getPercent(appRow[key]/appRow['Total']) + ")";
                    }
                }

            });
        });

        var teamMap = {}, daysOld;
        $scope.filterPortfolioApps.forEach(function(app) {
            if (!teamMap[app.teamId]) {
                teamMap[app.teamId] = {
                    name: "Team: " + app.teamName,
                    noOfScans: 0,
                    criticality: "",
                    daysScanedOld : 'Never',
                    apps : []
                };
            }
            if (app.noOfScans && app.latestScanTime) {
                teamMap[app.teamId].noOfScans += app.noOfScans;
                daysOld = Math.round((now.getTime() - app.latestScanTime)/(24*60*60*1000));
                if (!teamMap[app.teamId].lowBound || !teamMap[app.teamId].upBound) {
                    teamMap[app.teamId].lowBound = daysOld; teamMap[app.teamId].upBound = daysOld;
                } else {
                    if (teamMap[app.teamId].lowBound > daysOld) teamMap[app.teamId].lowBound = daysOld;
                    if (teamMap[app.teamId].upBound < daysOld) teamMap[app.teamId].upBound = daysOld;
                }
                app.daysScanedOld = daysOld;
            }

            teamMap[app.teamId].apps.push(app);
        });

        $scope.teamStatistics = [];
        Object.keys(teamMap).forEach(function(key){
            if (teamMap[key].noOfScans !== 0) {
                teamMap[key].daysScanedOld = (teamMap[key].lowBound === teamMap[key].upBound) ?
                    teamMap[key].lowBound : teamMap[key].lowBound + '-' + teamMap[key].upBound;
            }
            $scope.teamStatistics.push(teamMap[key]);
        });

        $scope.teamStatistics.sort(function(a, b) {
            return a.name.localeCompare(b.name);
        });

        $scope.scanStatistics = [];
        $scope.noOfScans = 0;
        $scope.teamStatistics.forEach(function(team){
            $scope.noOfScans += team.noOfScans;
            $scope.scanStatistics.push(team);
            team.apps.forEach(function(app) {
                app.name = app.appName;
                $scope.scanStatistics.push(app);
            });
            $scope.scanStatistics.push({});
        })


    };

    var filterMVABySeverity = function() {

        $scope.topAppsData = angular.copy($scope._topAppsData);

        $scope.topAppsData.forEach(function(app) {
            if (!$scope.parameters.severities.critical)
                app["Critical"] = 0;
            if (!$scope.parameters.severities.high)
                app["High"] = 0;
            if (!$scope.parameters.severities.medium)
                app["Medium"] = 0;
            if (!$scope.parameters.severities.low)
                app["Low"] = 0;
            if (!$scope.parameters.severities.info)
                app["Info"] = 0;
        });

        $scope.topAppsData.sort(function(app1, app2) {
            return getTotalVulns(app2) - getTotalVulns(app1);
        })
    };

    var getTotalVulns = function (app){
        return app.Critical + app.High + app.Medium + app.Low + app.Info;
    };

    var endsWith = function(str, suffix) {
        return str.indexOf(suffix, str.length - suffix.length) !== -1;
    };

    var beginsWith = function(str, prefix) {
        return str.indexOf(prefix) == 0;
    };

    $scope.exportPNG = function(isPDF){

        if (!$scope.exportInfo) {
            $scope.exportInfo = {
                id: $scope.reportId
            }
        } else {
            if ($scope.exportInfo.id  === $scope.reportId)
                $scope.exportInfo.id  = "" +  $scope.reportId;
            else
                $scope.exportInfo.id  = $scope.reportId;
        }
        $scope.exportInfo.isPDF = isPDF;
    };

    $scope.exportPDF = function() {

        var parameters = angular.copy($scope.parameters);

        if ($scope.reportId == $scope.OWASP_Report_Id)
            parameters.owasp = parameters.selectedOwasp;

        $scope.exportInfo = {};
        $scope.exportInfo.tableId = $scope.htmlElementIdMap[$scope.reportId];
        $scope.exportInfo.svgId = $scope.graphIdMap[$scope.reportId];
        $scope.exportInfo.title = $scope.titleMap[$scope.reportId];
        $scope.exportInfo.tags = $scope.title.tags;
        $scope.exportInfo.teams = $scope.title.teams;
        $scope.exportInfo.apps = $scope.title.apps;

        if ($scope.reportId === $scope.PIT_Report_Id || $scope.reportId === $scope.OWASP_Report_Id)
            reportExporter.exportPDFTable($scope, parameters, $scope.exportInfo);
        else
            reportExporter.exportPDFTableFromId($scope, $scope.exportInfo);

    }

    $scope.exportCSV = function() {

        var tagsName = ($scope.title.tags) ? "_" + $scope.title.tags : "";
        var teamsName = ($scope.title.teams) ? "_" + $scope.title.teams : "";
        var appsName = ($scope.title.apps) ? "_" + $scope.title.apps : "";

        reportExporter.exportCSV(convertToCsv($scope.title, $scope.progressByTypeData),
            "application/octet-stream", "VulnerabilityProgressByType" + teamsName + appsName + ".csv");
    };

    var convertToCsv = function(title, data){
        var csvArray = ['Vulnerability Progress By Type \n'];
        if (title.tags)
            csvArray.push(["Tags: " + title.tags]);
        if (title.teams)
            csvArray.push(["Teams: " + title.teams]);
        if (title.apps)
            csvArray.push(["Applications: " + title.apps]);
        csvArray.push("\n");
        csvArray.push(['Type,Count,% Fixed,Average Age,Average Time to Close']);
        data.forEach(function(d) {
            csvArray.push(d.description + ',' + d.total + ',' + d.percentClosed + ',' + d.averageAgeOpen + ',' + d.averageTimeToClose);
        });
        return csvArray.join("\n");
    };

});
