<%@ include file="/common/taglibs.jsp"%>
<head>
    <style>
        .report-text {
            text-align: center;
            margin-top: 115px;
            font-size: 17px;
            font-weight:bold;
            line-height:20px;
        }
    </style>
</head>

<div class="span6 ng-scope" ng-controller="CommonVulnerabilitiesController">

    <h4 class="ng-binding">{{ heading }}</h4>
    <div class="commVulns">

    </div>

</div>
