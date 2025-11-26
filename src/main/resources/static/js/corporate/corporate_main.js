(function () {
    const hamburger = document.querySelector(".hamburger");
    const sidebar = document.querySelector(".sidebar");
    let currentSel = sidebar.querySelector("li.selected");

    hamburger.addEventListener("click", () => {
        hamburger.classList.toggle("active");
        sidebar.classList.toggle("active");
    });

    // 외부 클릭 시 닫기
    document.addEventListener("click", (e) => {
        if (
            sidebar.classList.contains("active") &&
            !sidebar.contains(e.target) &&
            !hamburger.contains(e.target)
        ) {
            sidebar.classList.remove("active");
            hamburger.classList.remove("active");
        }
    });

    // 페이지에 맞게 메뉴 강조
    window.initAdminHeader = function (key) {
        const next = sidebar.querySelector(`li[data-key="${key}"]`);
        if (!next) return;

        currentSel?.classList.remove("selected");
        next.classList.add("selected");
        currentSel = next;
    };
})();
