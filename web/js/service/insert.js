function insert() {
    let input = $("#file")[0];
    let file = input.files[0];
    if (file === undefined) {
        alert('Video obrigatório');
        return;
    }

    const formData = new FormData();
    const metadata = {
        idVideo: 1,
        name: file.name,
        length: file.size,
        email: "brunogressler1@gmail.com"
    };
    formData.append("file", file);
    formData.append("metadata", new Blob(
            [JSON.stringify(metadata)],
            {type: "application/json"}
    ));
    $.ajax({
        url: "http://localhost:1010/v1/video",
        type: "POST",
        data: formData,
        processData: false, // impede que o jQuery tente processar o FormData
        contentType: false, // deixa o browser definir o cabeçalho Content-Type corretamente
        success: function (response) {
            console.log(response);
            //window.location.href = `${protocol}//${host}:${port}/${projectName}/loading.html`;
        },
        error: function (responseError) {
            console.error("Error: ", responseError);
            alert("Erro ao enviar vídeo.");
        },
        complete: function () {
        }
    });
}