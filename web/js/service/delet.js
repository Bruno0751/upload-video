function delet(id) {
    $.ajax({
        url: `ServletVideo?opcao=delete&id=${id}`,
        type: "DELETE",
        success: function (response, xhr) {
            const contentType = xhr.getResponseHeader("Content-Type");

            if (contentType && contentType.includes("application/json")) {
                if (response.status === "success") {
                    alert("Succees");
                    window.location.reload();
                } else {
                    alert("Erro: " + response.msg);
                }
            } else {
                alert("Response not found");
            }
        },
        error: function (responseError) {
            console.error("Error:", responseError);
            alert("Erro ao deletar vídeo.");
        },
        complete: function () {
        }
    });
}