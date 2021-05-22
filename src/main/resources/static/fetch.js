const usersURL = 'http://localhost:8081/api/admin'
const rolesURL = 'http://localhost:8081/api/admin/roles'
let rolesBuffer

/*
    HTTP Requests
*/
function getRequest(url) {
    const headers = {
        'Content-Type': 'application/json'
    }
    return fetch(url, {
        method: 'GET',
        headers: headers
    })
        .then(response => response.json())
        .catch(err => console.error(err))
}

function postRequest(url, method, body = null) {
    const headers = {
        'Content-Type': 'application/json'
    }
    return fetch(url, {
        method: "POST",
        body: JSON.stringify(body),
        headers: headers
    }).then(response => {
        if (response.ok) {
            return response.json()
        }
        return response.json().then(error => {
            const e = new Error('Something wrong while POST request')
            e.data = error
            throw e
        })
    })
}

function putRequest(url, method, body = null) {
    const headers = {
        'Content-Type': 'application/json'
    }
    return fetch(url, {
        method: "PUT",
        body: JSON.stringify(body),
        headers: headers
    }).then(response => {
        if (response.ok) {
            return response.json()
        }
        return response.json().then(error => {
            const e = new Error('Something wrong while PUT request')
            e.data = error
            throw e
        })
    })
}

function deleteRequest(url) {
    const headers = {
        'Content-Type': 'application/json',
    }
    return fetch(url, {
        method: 'DELETE',
        headers: headers
    })
        .then(response => console.log(response))
        .catch(err => console.error(err))
}

/*
    Initialization
*/
function documentReady() {
    document.getElementById("editCheckbox").innerHTML = ''
    document.getElementById("addCheckbox").innerHTML = ''
    document.getElementById("deleteCheckbox").innerHTML = ''

    getRequest(usersURL).then(data => insertUsersData(data))
    getRequest(rolesURL).then(data => {
        rolesBuffer = data
        createCheckbox(data, "editCheckbox")
        createCheckbox(data, "addCheckbox")
        createCheckbox(data, "deleteCheckbox")
    })
}

function insertUsersData(data) {
    for (let i = 0; i < data.length; i++) {
        let tableRow = document.createElement("tr")
        tableRow.id = data[i].id
        tableRow.innerHTML = `
                    <td>${data[i].id}</td>
                    <td>${data[i].firstName}</td>
                    <td>${data[i].lastName}</td>
                    <td>${data[i].email}</td>
                    <td>${data[i].authProvider}</td>
                    <td style="color:darkorange">${data[i].roles.map(role => role.role).join(' ')}</td>
                    <td><button class='btn btn-info' data-toggle='modal' onclick='editModal()'>Edit</button></td>
                    <td><button class='btn btn-danger' data-toggle='modal' onclick='deleteModal()'>Delete</button></td>
                    `
        document.querySelector('#usersData').appendChild(tableRow)
    }
}

function createCheckbox(data, elementName) {
    const rolesCheckbox = document.getElementById(elementName)

    for (let i = 0; i < data.length; i++) {
        let formCheckInline = document.createElement("div")
        formCheckInline.setAttribute("class", "form-check form-check-inline mx-0")

        let checkbox = document.createElement("input")
        checkbox.setAttribute("class", "form-check-input mx-0")
        checkbox.setAttribute("type", "checkbox")
        checkbox.setAttribute("value", "")
        checkbox.setAttribute("id", rolesBuffer[i].id + elementName)
        if (elementName === 'deleteCheckbox') {
            checkbox.disabled = true
        }

        let label = document.createElement("label")
        label.setAttribute("class", "form-check-label h6 mx-0")
        label.innerText = rolesBuffer[i].role

        formCheckInline.appendChild(label)
        formCheckInline.appendChild(checkbox)
        rolesCheckbox.appendChild(formCheckInline)

        checkbox.addEventListener('change', () => {
            if (checkbox.hasAttribute("checked")) {
                checkbox.removeAttribute("checked")
            } else {
                checkbox.setAttribute("checked", "true")
            }
        })
    }
}

/*
    Edit user
*/
function editModal() {

    $("#editModal").modal('show')
    let id = event.target.parentNode.parentNode.id

    getRequest(usersURL + '/' + id)
        .then(data => {
            $("#idEdit").val(data.id)
            $("#firstNameEdit").val(data.firstName)
            $("#lastNameEdit").val(data.lastName)
            $("#emailEdit").val(data.email)
            $("#birthdayEdit").val(data.birthday)
            $("#addressEdit").val(data.address)
            $("#passwordEdit").val(data.password)

            rolesBuffer.forEach(roleFromBuffer => {
                data.roles.forEach(role => {
                    if (role.role === roleFromBuffer.role) {
                        document.getElementById(roleFromBuffer.id + "editCheckbox")
                            .setAttribute("checked", "true")
                    }
                })
            })
        })
}

function editUser() {

    let rolesArray = []
    rolesBuffer.forEach(role => {
        if (document.getElementById(role.id + "editCheckbox")
            .hasAttribute("checked")) {
            rolesArray.push({
                id: role.id,
                role: role.role
            })
        }
    })

    const body = {
        id: $("#idEdit").val(),
        firstName: $("#firstNameEdit").val(),
        lastName: $("#lastNameEdit").val(),
        email: $("#emailEdit").val(),
        birthday: $("#birthdayEdit").val(),
        address: $("#addressEdit").val(),
        password: $("#passwordEdit").val(),
        roles: rolesArray
    }

    putRequest(usersURL, 'PUT', body)
        .then(data => {
            document.getElementById("usersData").innerHTML = ''
            documentReady()
        })
        .then($("#editModal").modal('hide'))
}

/*
    Delete user
*/
function deleteModal() {

    $("#deleteModal").modal("show")
    let id = event.target.parentNode.parentNode.id

    getRequest(usersURL + '/' + id)
        .then(data => {
            $("#idDelete").val(data.id)
            $("#firstNameDelete").val(data.firstName)
            $("#lastNameDelete").val(data.lastName)
            $("#emailDelete").val(data.email)
            $("#birthdayDelete").val(data.birthday)
            $("#addressDelete").val(data.address)
            $("#passwordDelete").val(data.password)

            rolesBuffer.forEach(roleFromBuffer => {
                data.roles.forEach(role => {
                    if (role.role === roleFromBuffer.role) {
                        document.getElementById(roleFromBuffer.id + "deleteCheckbox")
                            .setAttribute("checked", "true")
                    }
                })
            })
        })
}

function deleteUser() {
    deleteRequest(usersURL + '/' + $("#idDelete").val())
        .then(data => {
            document.getElementById("usersData").innerHTML = ''
            documentReady()
        })
        .then($("#deleteModal").modal('hide'))
}

/*
    Add user
*/
function addUser() {

    let rolesArray = []
    rolesBuffer.forEach(role => {
        if (document.getElementById(role.id + "addCheckbox")
            .hasAttribute("checked")) {
            rolesArray.push(role)
        }
    })

    const body = {
        firstName: $("#firstNameNew").val(),
        lastName: $("#lastNameNew").val(),
        email: $("#emailNew").val(),
        birthday: $("#birthdayNew").val(),
        password: $("#passwordNew").val(),
        roles: rolesArray
    }

    postRequest(usersURL, 'POST', body)
        .then(data => {
            document.getElementById("usersData").innerHTML = ''
            documentReady()
        })
        .then(() => {
            $("#firstNameNew").val('')
            $("#lastNameNew").val('')
            $("#emailNew").val('')
            $("#birthdayNew").val('')
            $("#passwordNew").val('')
        })
}

documentReady()
