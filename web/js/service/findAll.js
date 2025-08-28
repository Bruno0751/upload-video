async function findAll() {
    fetch(`ServletVideo?opcao=find`, {
        method: "GET"
    })
            .then(response => {
                if (response.status === 200) {
                    if (response.headers.get("Content-Type").includes('application/json;charset=UTF-8')) {
                        return response.json();
                    }
                } else {
                    alert(response.status);
                }
                throw new Error("Tipo de resposta inesperado:");
            })
            .then(data => {
                if (data.status === 'success') {
                    let div = window.document.getElementById("table");
                    let tableHTML = null;
                    if (data.record.length > 0) {
                        tableHTML = "<table class='table'>\n\
                                    <thead>\n\
                                        <tr>\n\
                                            <th scope='col'>Id</th>\n\
                                            <th scope='col'>Name</th>\n\
                                            <th scope='col'>Data</th>\n\
                                            <th scope='col'>Length</th>\n\
                                            <th scope='col'>Deletar</th>\n\
                                            <th scope='col'>Play</th>\n\
                                        </tr>\n\
                                    </thead>\n\
                                <tbody>\n";
                        for (let i = 0; i < data.record.length; i++) {
                            tableHTML += "<tr>\n\
                                        <td>" + data.record[i].idVideo + "</td>\n\
                                        <td>" + data.record[i].name + "</td>\n\
                                        <td>" + data.record[i].dateTime + "</td>\n\
                                        <td>" + data.record[i].length + "</td>\n\
                                        <td><button type='button' onClick='deletar(" + data.record[i].idVideo + ")' class='btn btn-danger'>Deletar</button></td>\n\
                                        <td><button type='submit' id='openPopup' onClick='loadVideo(" + data.record[i].idVideo + ")' class='btn btn-primary'>Play</button></td>\n\
                                    </tr>\n";
                        }
                        tableHTML += "</tbody>\n\
                                    <tfoot>\n\
                                        <tr>\n\
                                            <th scope='col'>Id</th>\n\
                                            <th scope='col'>Name</th>\n\
                                            <th scope='col'>Data</th>\n\
                                            <th scope='col'>Length</th>\n\
                                            <th scope='col'>Deletar</th>\n\
                                            <th scope='col'>Play</th>\n\
                                        </tr>\n\
                                    </tfoot>\n\
                                </table>";
                    } else {
                        tableHTML = '<h2>Lista Vazia<h2>';
                    }
                    div.innerHTML = tableHTML;
                } else {
                    alert('Hove algum erro');
                }
            })
            .catch(erro => {
                console.error("Erro:", erro);
                alert(erro);
            });
}