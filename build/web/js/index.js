function loadVideo(id) {
    fetch(`ServletVideo?opcao=streamVideo&id=${id}`, {
        method: "GET"
    })
            .then(response => {
                console.log(response.headers.get("Content-Type"));
                if (response.headers.get("Content-Type").includes('application/json;charset=UTF-8')) {
                    return response.json().then(json => {
                        if (json.status === 'error') {
                            throw new Error(json.msg);
                        }
                    });
                }
                if (response.headers.get("Content-Type").includes('video/mp4')) {
                    return response.blob();
                }
                throw new Error("Tipo de resposta inesperado:");
            })
            .then(blob => {
                const video = document.getElementById("videoPlayer");

                const videoUrl = URL.createObjectURL(blob);

                video.src = videoUrl;
                video.style.display = "block";
                video.load();
                video.play();
            })
            .catch(erro => {
                console.error("Erro:", erro);
                alert(erro);
            });
}
function deletar(id) {
    $.ajax({
        url: `ServletVideo?opcao=delete&id=${id}`,
        type: "DELETE",
        success: function (data, textStatus, xhr) {
            const contentType = xhr.getResponseHeader("Content-Type");

            if (contentType && contentType.includes("application/json")) {
                if (data.status === "success") {
                    alert("Vídeo deletado com sucesso!");
                } else {
                    alert("Erro: " + data.msg);
                }
            } else {
                alert("Resposta inesperada do servidor.");
            }
        },
        error: function (xhr, status, error) {
            console.error("Erro ao deletar vídeo:", error);
            alert("Erro ao deletar vídeo.");
        },
        complete: function () {
            console.log("Requisição DELETE finalizada.");
        }
    });
}
