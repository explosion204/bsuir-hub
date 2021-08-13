<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="bht" uri="bsuirhub-tags" %>

<html>
<head>
  <title><bht:localeTag key="contact_us.contact_us" /></title>
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
      <bht:localeTag key="contact_us.message_sent" />
    </div>
  </c:if>
  <c:if test="${validationError}">
    <div class="alert alert-danger" role="alert">
      <bht:localeTag key="model.validation_error" />
    </div>
  </c:if>


  <h4 class="pb-2"><bht:localeTag key="contact_us.contact_us" /></h4>
  <form id="contactForm" method="post" action="/send_message">
    <div class="form-group mb-2 mt-2">
      <label for="emailInput"><bht:localeTag key="contact_us.your_contact_email" /></label>
      <input type="email" name="email" class="bg-light form-control" maxlength="50" id="emailInput" required>
      <div class="invalid-feedback">
        <bht:localeTag key="contact_us.invalid_email" />
      </div>
    </div>
    <div class="form-group mb-2 mt-2">
      <label for="messageInput"><bht:localeTag key="contact_us.message" /></label>
      <textarea name="text" class="bg-light form-control" maxlength="255" id="messageInput" required></textarea>
      <div class="invalid-feedback">
        <bht:localeTag key="contact_us.message_validation" />
      </div>
    </div>
    <div class="py-2 pb-3">
      <button type="submit" class="btn btn-secondary w-100" id="sendEmailButton" disabled><bht:localeTag key="contact_us.send_message" /></button>
    </div>
  </form>
</div>

<jsp:include page="shared/footer.jsp"/>
</body>
</html>
