function insert() {
    let input = $("#file")[0];
    let file = input.files[0];

    if (file === undefined) {
        alert('Video obrigatório');
        return;
    }

    const formData = new FormData();
    formData.append("video", file);

    $.ajax({
        url: "ServletVideo?opcao=insert",
        type: "POST",
        data: formData,
        processData: false, // impede que o jQuery tente processar o FormData
        contentType: false, // deixa o browser definir o cabeçalho Content-Type corretamente
        success: function (data, textStatus, xhr) {
            if (xhr.getResponseHeader("Content-Type").includes("application/json")) {
                if (data.status === "success") {
                    window.location.href = `${protocol}//${host}:${port}/${projectName}/loading.html`;
                } else {
                    alert("Erro: " + data.msg);
                }
            } else {
                alert("Resposta inesperada do servidor.");
            }
        },
        error: function (xhr, status, error) {
            console.log(error);
            console.error("Error", error);
            alert("Erro ao enviar vídeo.");
        },
        complete: function () {

        }
    });
}