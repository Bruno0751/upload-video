async function findAll() {
    try {
        const response = await fetch(`${servlet}find`);
        if (!response.ok) {
            throw new Error(response.status);
        }
        if (!response.headers.get("Content-Type")?.includes("application/json")) {
            throw new Error("Resposta não é JSON");
        }
        const data = await response.json();
        if (data.status !== "success") {
            throw new Error("Erro no retorno");
        }
        console.log(data)
        renderTable(data.record);
    } catch (erro) {
        console.error("Erro:", erro);
        alert(erro);
    }
}
function renderTable(records) {
    const div = document.getElementById("table");
    if (records.length === 0) {
        div.innerHTML = "<h2>Lista Vazia</h2>";
        return;
    }
    let table;
    table = "<table class='table'>\n\
        <thead>\n\
                <tr>\n\
                    <th scope='col'>Id</th>\n\
                    <th scope='col'>Name</th>\n\
                    <th scope='col'>Data</th>\n\
                    <th scope='col'>Length</th>\n\
                    <th scope='col'>Email</th>\n\
                    <th scope='col'>Deletar</th>\n\
                    <th scope='col'>Play</th>\n\
                </tr>\n\
        </thead>\n\
        <tbody>";
        for (let i = 0; i < records.length; i++) {
            table += "<tr>\n\
                <td>" + records[i].idVideo + "</td>\n\
                <td>" + records[i].name + "</td>\n\
                <td>" + records[i].date + "</td>\n\
                <td>" + records[i].length + "</td>\n\
                <td>" + records[i].email + "</td>\n\
                <td><button type='button' onClick=delet('" + records[i].id + "') class='btn btn-danger'>Deletar</button></td>\n\
                <td><button type='submit' data-id='" + records[i].idVideo + "' onClick='loadVideo(" + records[i].idVideo + ")' class='btn btn-primary openPopup'>Play</button></td>\n\
            </tr>";
        }
        table += "</tbody><tfoot>\n\
            <tr>\n\
                <th scope='col'>Id</th>\n\
                <th scope='col'>Name</th>\n\
                <th scope='col'>Data</th>\n\
                <th scope='col'>Length</th>\n\
                <th scope='col'>Email</th>\n\
                <th scope='col'>Deletar</th>\n\
                <th scope='col'>Play</th>\n\
            </tr>\n\
        </tfoot>\n\
    </table>";
    div.innerHTML = table;
}