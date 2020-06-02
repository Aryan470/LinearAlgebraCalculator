function processExpression() {
    let form = document.getElementById("inputForm");
    let expression = form.elements.namedItem("expression").value;

    const data = new FormData();
    data.append("expression", expression);

    const xhr = new XMLHttpRequest();
    const url = "/query";
    xhr.open("POST", url, true);
    xhr.onreadystatechange = function () {
        if (xhr.readyState == 4) {
            processResults(xhr.responseText);
        }
    };
    xhr.send(data);
}

function processResults(responseText) {
    let response = JSON.parse(responseText);
    const type = response["type"];
    let result = "NO RESULT";
    if (type === "report") {
        if (response.message) {
            result = name + "<br>" + response.message;
        } else {
            result = drawContent(response.content);
        }
    } else if (type === "scalar") {
        result = drawNumber(response.value);
    } else if (type === "vector") {
        result = drawVector(response.components);

    } else if (type === "matrix") {
        result = drawMatrix(response.components);
    } else if (type === "function") {
        result = drawFunction(response);
    }
    document.getElementById("output").innerText = result;
    MathJax.typeset();
}

function drawFunction(fullFunc) {
    return "$" + fullFunc + "$";
}

function drawContent(content) {
    let output = "";
    Object.keys(content).forEach(key => {
        let thisType = content[key]["type"];

        if (thisType === "function") {
            output = content[key].full;
        } else {
            let result;
            if (thisType === "scalar") {
                result = key + " = " + drawNumber(content[key].value);
            } else if (thisType === "vector") {
                result = drawVector(content[key].components);
            } else if (thisType === "matrix") {
                result = drawMatrix(content[key].components);
            }
            output += key + " = " + result
        }
        output += "\n";
    });
    return output;
}

function drawNumber(number) {
    return "$" + number + "$"
}

function drawVector(components) {
    let out = "$\\begin{bmatrix}";
    components.forEach(item => {
        out += item + "\\\\";
    });
    out += "\\end{bmatrix}$";
    return out;
}

function drawMatrix(components) {
    let out = "$\\begin{bmatrix}";
    components.forEach(row => {
        row.forEach(cell => {
            out += cell + "&";
        });
        out = out.slice(0, -1);
        out += "\\\\";
    });
    out += "\\end{bmatrix}$"
    return out;
}