<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="locale" />

<footer>
    <div class="text-center p-4" style="background-color: rgba(0, 0, 0, 0.05);">
        2021, Karnyshov Dmitry:
        <i class="fab fa-github"></i>
        <a class="text-reset fw-bold" href="https://github.com/explosion204/bsuir-hub" target="_blank">Github</a>
        <div class="dropdown mt-2">
            <a class="btn btn-secondary dropdown-toggle" href="#" role="button" id="dropdownMenuLink" data-bs-toggle="dropdown" aria-expanded="false">
                <fmt:message key="footer.lang" /> - <jsp:text>${sessionScope.locale}</jsp:text>
            </a>

            <ul class="dropdown-menu" aria-labelledby="dropdownMenuLink">
                <li><button id="ru_locale" class="dropdown-item" onclick="localeItemClick('ru');">Russian</button></li>
                <li><button id="en_locale" class="dropdown-item" onclick="localeItemClick('en');">English</button></li>
            </ul>
        </div>
    </div>
</footer>

<script>
    function localeItemClick(localeCode) {
        <c:choose>
            <c:when test="${cookie.size() eq 0}">
                $.confirm({
                    title: 'Внимание! Attention!',
                    content: 'Выбран язык по умолчанию, так как куки отключены!<br/>Default language is set because of disabled cookies!',
                    buttons: {
                        'OK': function () { },
                    }
                });
            </c:when>
            <c:otherwise>
                setLocale(localeCode);
            </c:otherwise>
        </c:choose>
    }
</script>