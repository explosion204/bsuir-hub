<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <jsp:include page="shared/head.html" />
</head>
<body>
    <jsp:include page="shared/header.jsp" />

    <div class="container login-container mt-auto mb-auto">
        <div class="row justify-content-center">
            <div class="col-md-6 login-form">
                <form class="needs-validation" method="post" action="/login" novalidate>
                    <div class="form-group mb-3">
                        <input type="text" class="form-control" placeholder="Login" name="login" required />
                        <div class="invalid-feedback">This field is required</div>
                    </div>
                    <div class="form-group mb-3">
                        <input type="password" class="form-control" placeholder="Password" name="password" required />
                        <div class="invalid-feedback">This field is required</div>
                    </div>
                    <div class="form-group mb-3">
                        <input type="submit" class="btn-submit" value="Login" />
                    </div>
                    <c:if test="${not empty login_error}">
                        <div class="alert alert-danger" role="alert">
                            <jsp:text>${login_error}</jsp:text>
                        </div>
                    </c:if>
                    <div class="form-group">
                        <a href="#" class="forget-pwd">Forgot Password?</a>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <jsp:include page="shared/footer.jsp"/>
</body>
</html>
