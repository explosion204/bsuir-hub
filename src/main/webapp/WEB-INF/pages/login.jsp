<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <jsp:include page="shared/head.html" />
</head>
<body>
    <jsp:include page="shared/header.jsp" />

    <div class="container login-container mt-auto mb-auto">
        <div class="row justify-content-center">
            <div class="col-md-6 login-form">
                <form class="needs-validation" novalidate>
                    <div class="form-group mb-3">
                        <input type="text" class="form-control" placeholder="Login" value="" required />
                        <div class="invalid-feedback">This field is required</div>
                    </div>
                    <div class="form-group mb-3">
                        <input type="password" class="form-control" placeholder="Password" value="" required />
                        <div class="valid-feedback">OK</div>
                        <div class="invalid-feedback">This field is required</div>
                    </div>
                    <div class="form-group mb-3">
                        <input type="submit" class="btn-submit" value="Login" />
                    </div>
                    <div class="form-group">
                        <a href="#" class="forget-pwd">Forget Password?</a>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <jsp:include page="shared/footer.html"/>
</body>
</html>
