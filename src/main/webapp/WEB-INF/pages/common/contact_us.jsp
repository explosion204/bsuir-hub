<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <title>Settings</title> <!-- TODO titles -->
  <jsp:include page="shared/head.html" />
  <link href="/static/css/common/settings.css" rel="stylesheet">
  <script src="/static/js/util/validation.js"></script>
  <script src="/static/js/common/contact_us.js"></script>
</head>
<body>
<jsp:include page="shared/header.jsp" />

<div class="wrapper bg-white mt-auto mb-auto w-100 p-4">
  <c:if test="${messageSent}">
    <div class="alert alert-success mt-2" role="alert">
      Message successfully send, we will contact you soon
    </div>
  </c:if>
  <c:if test="${validationError}">
    <div class="alert alert-danger" role="alert">
      A server-side validation error occurred
    </div>
  </c:if>


  <h4 class="pb-2">Contact us</h4>
  <form id="contactForm" method="post" action="/send_message">
    <div class="form-group mb-2 mt-2">
      <label for="emailInput">Your contact email</label>
      <input type="email" name="email" class="bg-light form-control" maxlength="50" id="emailInput" required>
      <div class="invalid-feedback">
        Invalid email
      </div>
    </div>
    <div class="form-group mb-2 mt-2">
      <label for="messageInput">Message</label>
      <textarea name="text" class="bg-light form-control" maxlength="255" id="messageInput" required></textarea>
      <div class="invalid-feedback">
        Message text must have 1 - 255 characters
      </div>
    </div>
    <div class="py-2 pb-3">
      <button type="submit" class="btn btn-secondary w-100" id="sendEmailButton" disabled>Send message</button>
    </div>
  </form>
</div>

<jsp:include page="shared/footer.jsp"/>
</body>
</html>
