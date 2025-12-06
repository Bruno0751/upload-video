function loadScript(src) {
  return new Promise((resolve, reject) => {
    const script = document.createElement("script");
    script.src = src;
    script.onload = resolve;
    script.onerror = reject;
    document.head.appendChild(script);
  });
}

(async () => {
  try {
    await loadScript("js/variables.js");
  } catch (e) {
    console.error("Erro ao carregar scripts:", e);
  }
})();