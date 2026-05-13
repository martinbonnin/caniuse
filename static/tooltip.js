(() => {
  let tooltipEl = null;
  let activeBadge = null;

  function ensureTooltip() {
    if (tooltipEl) return tooltipEl;
    tooltipEl = document.createElement("div");
    tooltipEl.className = "tooltip";
    tooltipEl.setAttribute("role", "tooltip");
    document.body.appendChild(tooltipEl);
    return tooltipEl;
  }

  function position(badge) {
    const tip = ensureTooltip();
    tip.textContent = badge.getAttribute("data-tooltip") || "";

    const margin = 8;
    const gap = 6;
    tip.style.maxWidth = Math.max(120, window.innerWidth - margin * 2) + "px";
    tip.style.left = "0px";
    tip.style.top = "0px";
    tip.style.visibility = "hidden";
    tip.style.display = "block";

    const tipRect = tip.getBoundingClientRect();
    const badgeRect = badge.getBoundingClientRect();

    let left = badgeRect.left + badgeRect.width / 2 - tipRect.width / 2;
    left = Math.max(margin, Math.min(left, window.innerWidth - tipRect.width - margin));

    let top = badgeRect.top - tipRect.height - gap;
    if (top < margin) {
      top = badgeRect.bottom + gap;
    }

    tip.style.left = left + "px";
    tip.style.top = top + "px";
    tip.style.visibility = "visible";
    tip.style.opacity = "1";
  }

  function show(badge) {
    activeBadge = badge;
    position(badge);
  }

  function hide(badge) {
    if (badge && activeBadge !== badge) return;
    activeBadge = null;
    if (tooltipEl) {
      tooltipEl.style.opacity = "0";
      tooltipEl.style.display = "none";
    }
  }

  function attach() {
    document.querySelectorAll(".badge-experimental[data-tooltip]").forEach((badge) => {
      badge.addEventListener("mouseenter", () => show(badge));
      badge.addEventListener("mouseleave", () => hide(badge));
      badge.addEventListener("focus", () => show(badge));
      badge.addEventListener("blur", () => hide(badge));
      badge.addEventListener("click", (e) => {
        e.stopPropagation();
        if (activeBadge === badge) {
          hide(badge);
        } else {
          show(badge);
        }
      });
    });

    window.addEventListener("scroll", () => {
      if (activeBadge) position(activeBadge);
    }, { passive: true });
    window.addEventListener("resize", () => {
      if (activeBadge) position(activeBadge);
    });
    document.addEventListener("click", (e) => {
      if (activeBadge && !activeBadge.contains(e.target)) hide(activeBadge);
    });
  }

  if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", attach);
  } else {
    attach();
  }
})();
