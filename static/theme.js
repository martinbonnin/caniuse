(() => {
  const themes = ["light", "dark", "system"];

  const icons = {
    light: '<svg viewBox="0 0 14 14" fill="currentColor" width="16" height="16"><path fill-rule="evenodd" clip-rule="evenodd" d="M7.58333 0H6.41667V2.33333H7.58333V0ZM0 6.41667V7.58333H2.33333V6.41667H0ZM11.6667 6.41667H14V7.58333H11.6667V6.41667ZM7.58333 14H6.41667V11.6667H7.58333V14ZM4.66667 3.5H9.33333V4.66667H4.66667V3.5ZM4.66667 9.33333H3.5V4.66667H4.66667V9.33333ZM4.66667 9.33333V10.5H9.33333V9.33333H10.5V4.66667H9.33333V9.33333H4.66667ZM11.6667 1.16667H12.8333V2.33333H11.6667V1.16667ZM11.6667 2.33333V3.5H10.5V2.33333H11.6667ZM12.8333 12.8333H11.6667V11.6667H12.8333V12.8333ZM11.6667 11.6667H10.5V10.5H11.6667V11.6667ZM2.33333 1.16667H1.16667V2.33333H2.33333V3.5H3.5V2.33333H2.33333V1.16667ZM1.16667 12.8333H2.33333V11.6667H3.5V10.5H2.33333V11.6667H1.16667V12.8333Z"/></svg>',
    dark: '<svg viewBox="0 0 14 14" fill="currentColor" width="16" height="16"><path d="M10.501 12.833H4.66797V11.667H10.501V12.833ZM4.66797 11.667H3.50098V10.5H4.66797V11.667ZM11.668 11.667H10.501V10.5H11.668V11.667ZM3.50098 10.5H2.33398V9.33301H3.50098V10.5ZM12.834 10.5H11.668V8.16699H10.501V7H11.668V5.83301H12.834V10.5ZM2.33398 9.33301H1.16797V3.5H2.33398V9.33301ZM10.501 9.33301H7.00098V8.16699H10.501V9.33301ZM7.00098 8.16699H5.83398V7H7.00098V8.16699ZM5.83398 7H4.66797V3.5H5.83398V7ZM3.50098 3.5H2.33398V2.33301H3.50098V3.5ZM8.16797 2.33301H7.00098V3.5H5.83398V2.33301H3.50098V1.16699H8.16797V2.33301Z"/></svg>',
    system: '<svg viewBox="0 0 14 14" fill="currentColor" width="16" height="16"><path d="M12.834 2.33301V10.5H9.33398V11.667H4.66797V10.5H1.16797V2.33301H12.834ZM2.33398 3.5V9.33301H11.668V3.5H2.33398Z"/></svg>',
    check: '<svg viewBox="0 0 24 24" fill="currentColor" width="16" height="16"><path d="M10 18H8V16H10V18ZM8 16H6V14H8V16ZM12 14V16H10V14H12ZM6 14H4V12H6V14ZM14 14H12V12H14V14ZM16 12H14V10H16V12ZM18 10H16V8H18V10ZM20 8H18V6H20V8Z"/></svg>',
  };

  function resolvedTheme() {
    const stored = localStorage.getItem("theme") || "system";
    if (stored !== "system") return stored;
    return window.matchMedia("(prefers-color-scheme: dark)").matches ? "dark" : "light";
  }

  function applyTheme(theme) {
    if (theme === "system") {
      document.documentElement.removeAttribute("data-theme");
    } else {
      document.documentElement.setAttribute("data-theme", theme);
    }
  }

  // Apply immediately to avoid flash
  const stored = localStorage.getItem("theme") || "system";
  applyTheme(stored);

  document.addEventListener("DOMContentLoaded", () => {
    const trigger = document.getElementById("theme-trigger");
    const menu = document.getElementById("theme-menu");
    let open = false;

    function currentTheme() {
      return localStorage.getItem("theme") || "system";
    }

    function updateTrigger() {
      trigger.innerHTML = icons[resolvedTheme()];
    }

    function renderMenu() {
      const selected = currentTheme();
      menu.innerHTML = themes
        .map(
          (t) =>
            `<button class="theme-option${t === selected ? " selected" : ""}" data-theme-value="${t}">
              <span class="theme-option-icon">${icons[t]}</span>
              <span class="theme-option-label">${t}</span>
              <span class="theme-option-check">${icons.check}</span>
            </button>`
        )
        .join("");
    }

    function toggle() {
      open = !open;
      menu.classList.toggle("open", open);
      trigger.setAttribute("aria-expanded", open);
    }

    function close() {
      open = false;
      menu.classList.remove("open");
      trigger.setAttribute("aria-expanded", "false");
    }

    updateTrigger();
    renderMenu();

    trigger.addEventListener("click", (e) => {
      e.stopPropagation();
      toggle();
    });

    menu.addEventListener("click", (e) => {
      const btn = e.target.closest("[data-theme-value]");
      if (!btn) return;
      const value = btn.dataset.themeValue;
      localStorage.setItem("theme", value);
      applyTheme(value);
      updateTrigger();
      renderMenu();
      close();
    });

    document.addEventListener("click", () => close());

    window
      .matchMedia("(prefers-color-scheme: dark)")
      .addEventListener("change", () => {
        if (currentTheme() === "system") {
          updateTrigger();
        }
      });
  });
})();
