const timestamp = new Date().getTime();

addLinkTag(`style/stleMain.css?v=${timestamp}`);
addLinkTag(`style/style-popup.css?v=${timestamp}`);

function addLinkTag(href) {
    const link = document.createElement("link");
    link.rel = "stylesheet";
    link.type = "text/css";
    link.href = href;
    document.head.appendChild(link);
}
