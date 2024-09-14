<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <!-- Navbar start -->
        <div class="container-fluid fixed-top bg-white shadow-sm">
            <div class="container px-0">
                <nav class="navbar navbar-light navbar-expand-xl py-3">
                    <a href="/" class="navbar-brand d-flex align-items-center">
                        <h1 class="text-primary display-6 mb-0 me-2">Laptopshop</h1>
                    </a>
                    <button class="navbar-toggler py-2 px-3" type="button" data-bs-toggle="collapse"
                        data-bs-target="#navbarCollapse">
                        <span class="fa fa-bars text-primary"></span>
                    </button>
                    <div class="collapse navbar-collapse bg-white justify-content-between mx-5" id="navbarCollapse">
                        <!-- Left Navigation Links -->
                        <div class="navbar-nav">
                            <a href="/" class="nav-item nav-link active text-dark fw-bold px-3">Trang Chủ</a>
                            <a href="/products" class="nav-item nav-link text-dark fw-bold px-3">Sản Phẩm</a>
                        </div>
                        <!-- Right Section (Cart, User Info) -->
                        <div class="d-flex align-items-center">
                            <!-- Cart Section -->
                            <c:if test="${not empty pageContext.request.userPrincipal}">
                                <a href="/cart" class="position-relative me-4">
                                    <i class="fa fa-shopping-bag fa-2x text-primary"></i>
                                    <span
                                        class="position-absolute bg-secondary rounded-circle d-flex align-items-center justify-content-center text-white"
                                        style="top: -5px; left: 15px; height: 20px; min-width: 20px; font-size: 12px;">
                                        ${sessionScope.sum}
                                    </span>
                                </a>
                                <!-- User Profile Dropdown -->
                                <div class="dropdown">
                                    <a href="#" class="dropdown-toggle text-dark" id="dropdownMenuLink"
                                        data-bs-toggle="dropdown" aria-expanded="false">
                                        <i class="fas fa-user fa-2x"></i>
                                    </a>
                                    <ul class="dropdown-menu dropdown-menu-end p-4 shadow-sm"
                                        aria-labelledby="dropdownMenuLink" style="min-width: 300px;">
                                        <li class="d-flex align-items-center flex-column">
                                            <img src="/images/avatar/${sessionScope.avatar}" alt="User Avatar"
                                                class="rounded-circle mb-3 shadow" style="width: 150px; height: 150px;">
                                            <div class="text-center fw-bold mb-2">
                                                ${sessionScope.fullName}
                                            </div>
                                        </li>
                                        <li><a class="dropdown-item" href="#">Quản lý tài khoản</a></li>
                                        <li><a class="dropdown-item" href="#">Lịch sử mua hàng</a></li>
                                        <li>
                                            <hr class="dropdown-divider">
                                        </li>
                                        <li>
                                            <form action="/logout" method="post">
                                                <input type="hidden" name="${_csrf.parameterName}"
                                                    value="${_csrf.token}" />
                                                <button class="dropdown-item">Đăng xuất</button>
                                            </form>
                                        </li>
                                    </ul>
                                </div>
                            </c:if>
                            <!-- Login Section -->
                            <c:if test="${empty pageContext.request.userPrincipal}">
                                <a href="/login" class="btn btn-primary text-white px-4 py-2 rounded-pill shadow-sm">
                                    Đăng nhập
                                </a>
                            </c:if>
                        </div>
                    </div>
                </nav>
            </div>
        </div>
        <!-- Navbar End -->