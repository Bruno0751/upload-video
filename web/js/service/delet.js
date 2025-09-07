function delet(id) {
    $.ajax({
        url: `ServletVideo?opcao=delete&id=${id}`,
        type: "DELETE",
        success: function (data, textStatus, xhr) {
            const contentType = xhr.getResponseHeader("Content-Type");

            if (contentType && contentType.includes("application/json")) {
                if (data.status === "success") {
                    alert("Succees");
                    window.location.reload();
                } else {
                    alert("Erro: " + data.msg);
                }
            } else {
                alert("Response not found");
            }
        },
        error: function (xhr, status, error) {
            console.error("Error:", error);
            alert("Error");
        },
        complete: function () {
        }
    });
}