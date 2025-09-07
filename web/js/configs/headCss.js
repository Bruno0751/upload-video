//const timestamp = Date.now();
const timestamp = new Date().getTime();
//const timestampSegundos = Math.floor(Date.now() / 1000);
document.title = "Meu Sistema de Videos";

const charset = document.createElement("meta");
charset.setAttribute("charset", "UTF-8");
document.head.appendChild(charset);

addMetaTag("viewport", "width=device-width, initial-scale=1.0");
addMetaTag("author", "Bruno Gressler da Silveira");
addMetaTag("description", "Nosso site é a plataforma ideal para quem busca cadastrar, gerenciar e manipular vídeos de forma simples, rápida e segura. Com uma interface intuitiva e moderna, você pode fazer upload dos seus vídeos, organizá-los em categorias personalizadas e editá-los com praticidade. Oferecemos diversas ferramentas para manipulação, como cortes, junções, ajustes de qualidade e até inserção de legendas, tudo online e sem a necessidade de softwares adicionais. Além disso, você pode definir títulos, descrições e tags otimizadas para cada vídeo, facilitando a busca e o engajamento do seu conteúdo. Nossa plataforma é totalmente responsiva, permitindo que você acesse e gerencie seus vídeos de qualquer dispositivo, seja computador, tablet ou smartphone. Se você deseja mais controle, praticidade e agilidade no gerenciamento dos seus vídeos, nosso site é a solução perfeita para você.");
addMetaTag("keywords", "video, videos, sistema, upload, uploads, pvideos");

addLinkTag(`style/stleMain.css?v=${timestamp}`);
addLinkTag(`style/style-popup.css?v=${timestamp}`);

function addMetaTag(name, content) {
    const meta = document.createElement("meta");
    meta.name = name;
    meta.content = content;
    document.head.appendChild(meta);
}

function addLinkTag(href) {
    const link = document.createElement("link");
    link.rel = "stylesheet";
    link.type = "text/css";
    link.href = href;
    document.head.appendChild(link);
}