<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 2020/12/3
  Time: 14:33
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="userId" value="${userId}" scope="session"></c:set>
<c:set var="userTel" value="${userTel}" scope="session"></c:set>
<c:set var="userName" value="${userName}"></c:set>
<script>
    function quitTeam(teamName,teamId)
    {
        if(confirm("是否确认退出"+teamName+"团队？您若是该团队的队长，则该团队将解散。"))
            window.location.href = "${pageContext.request.contextPath}/team/quitTeam/"+teamId;
        else
            window.location.href = "${pageContext.request.contextPath}/team/backToMyTeam";
    }
</script>
<html>
<head>
    <title>Work Together</title>
    <link href="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container-fluid">
        <div class="row-fluid">
            <div class="col-md-2 column">
                <div class="page-header">
                    <img src="${pageContext.request.contextPath}/img/logo.png" class="img-responsive" alt="加载失败">
                </div>
                <ul class="nav nav-list">
                    <li class="nav-header">
                        <h4>
                            页面导航
                        </h4>
                    </li>
                    <li class="active">
                        <a href="${pageContext.request.contextPath}"><span class="glyphicon glyphicon-home" aria-hidden="true"></span>首页</a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/team/myTeam/${userTel}"><span class="glyphicon glyphicon-grain" aria-hidden="true"></span> 我的团队</a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/task/myTask/${userId}"><span class="glyphicon glyphicon-th-list" aria-hidden="true"></span> 我的任务</a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/log/mylog/${userId}"><span class="glyphicon glyphicon-file" aria-hidden="true"></span> 日志</a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/post/AllPost/1">
                            <span class="glyphicon glyphicon-fire" aria-hidden="true"></span>
                            论坛
                        </a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/user/userDetail/${userId}"><span class="glyphicon glyphicon-heart" aria-hidden="true"></span> 个人中心</a>
                    </li>
                    <li class="divider">
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/user/goHelp"><span class="glyphicon glyphicon-question-sign" aria-hidden="true"></span>
                            帮助
                        </a>
                    </li>
                </ul>
            </div>
            <div class="col-md-10 column">
                <div class="page-header">
                    <h1>我的团队</h1>
                </div>

                <div class="col-md-2 column">
                    <a href="${pageContext.request.contextPath}/team/showAllTeam" class="btn btn-default active" role="button">加入团队</a>
                </div>
                <div class="col-md-2 column">
                    <a onclick="createTeam()" class="btn btn-default active" role="button">创建团队</a>
                    <script>
                        function createTeam()
                        {
                            var teamName = prompt("请输入团队名称：");
                            var teamNumberLimit = prompt("请输入团队成员上限(最多50人)：","5");
                            if(teamName != "" && teamName != null && teamNumberLimit != null)
                                window.location.href = "${pageContext.request.contextPath}/team/createMyTeam/"+teamName+"/"+teamNumberLimit;
                            else if(teamName == "" && teamNumberLimit != null)
                                window.alert("团队名称不能为空！创建团队失败");
                            else
                                window.location.href = "${pageContext.request.contextPath}/team/backToMyTeam";
                        }
                    </script>
                </div>
                <div class="col-md-2 column">
                    <a href="${pageContext.request.contextPath}/team/showLeadingTeam" class="btn btn-default active" role="button">管理团队</a>
                </div>
                <div class="col-md-2 column">
                    <a onclick="enterChatroom()" class="btn btn-default active" role="button">聊天室</a>
                    <script>
                        function enterChatroom() {
                            var nickname = prompt("请输入聊天名："+'${userName}')
                            if(nickname != null && "" != nickname )
                                window.location.href = "${pageContext.request.contextPath}/chatroom/enter/${userId}/"+nickname;
                        }
                    </script>
                </div>
                <div class="col-md-4 column">
                    <form class="form-inline" action="${pageContext.request.contextPath}/team/findMyTeamByKeyword" method="post" align="center">
                        <input type="text" name="keyword" class="form-control" placeholder="请输入团队名称"/>
                        <button type="submit" class="btn btn-default active">查找</button>
                    </form>
                </div>

                <table class="table table-hover table-striped">
                    <thead>
                    <tr>
                        <th>团队名称</th>
                        <th style="vertical-align: middle !important;text-align: center;">队长</th>
                        <th style="vertical-align: middle !important;text-align: center;">成员数量/成员上限</th>
                    </tr>
                    </thead>

                    <tbody>
                    <c:forEach var="team" items="${teams}">
                        <tr>
                            <td style="vertical-align: middle">${team.teamName}</td>
                            <td style="vertical-align: middle ;text-align: center;">${team.leaderName}</td>
                            <td style="vertical-align: middle ;text-align: center;">${team.memberNum}/${team.memberNumLimit}</td>
                            <td style="vertical-align: middle;text-align: center;">
                                <a href="${pageContext.request.contextPath}/team/teamDetail/${team.teamId}" class="btn btn-default active" role="button">查看详情</a>
                            </td>
                            <td style="vertical-align: middle;text-align: center;">
                                <a onclick="quitTeam('${team.teamName}',${team.teamId})" class="btn btn-default active" role="button">退出团队</a>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</body>
</html>
