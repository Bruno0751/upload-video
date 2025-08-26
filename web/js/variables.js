const dataNavigator = window.location;
const url = new URL(dataNavigator.href);

const protocol = window.location.protocol;
const host = window.location.hostname;
const port = window.location.port;
const projectName = url.pathname.split('/').filter(Boolean)[0];

console.log(`${protocol}//${host}:${port}/${projectName}/file.html`);