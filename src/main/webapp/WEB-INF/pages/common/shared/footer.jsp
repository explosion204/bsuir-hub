<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="bht" uri="bsuirhub-tags" %>

<footer>
    <div class="text-center p-4" style="background-color: rgba(0, 0, 0, 0.05);">
        2021, Karnyshov Dmitry:
        <i class="fab fa-github"></i>
        <a class="text-reset fw-bold" href="https://github.com/explosion204/bsuir-hub" target="_blank">Github</a>
        <div class="dropdown mt-2">
            <a class="btn btn-secondary dropdown-toggle" href="#" role="button" id="dropdownMenuLink" data-bs-toggle="dropdown" aria-expanded="false">
                <bht:localeTag key="footer.lang" /> - ${cookie['localeCode'].value}
            </a>

            <ul class="dropdown-menu" aria-labelledby="dropdownMenuLink">
                <li><button id="ru_locale" class="dropdown-item" onclick="setLocale('ru');">Russian</button></li>
                <li><button id="en_locale" class="dropdown-item" onclick="setLocale('en');">English</button></li>
            </ul>
        </div>
    </div>
</footer>