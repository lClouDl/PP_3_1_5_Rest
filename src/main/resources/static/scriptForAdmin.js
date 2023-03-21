/*URL*/
const thisUserUrl = 'http://localhost:8080/api/users/thisUser';
const linkUsersUrl = 'http://localhost:8080/api/users';
const addUrl = 'http://localhost:8080/api/users';

/*Объект промежуточного состояния. Будет хранить в себе объект user, поля которого будут заполняться из input*/
let state = {
    id: undefined,
    firstName: undefined,
    lastName: undefined,
    gender: undefined,
    login: undefined,
    roleSet: undefined,
    password: undefined
}

/*Заполнение страницы User*/

const loadUser = async () => {
    const response = await fetch(thisUserUrl);
    return await response.json();
}
let thisUser = await loadUser();

/*Заполнение шапки страницы*/

document.getElementById("nameNavBar").innerHTML = thisUser.firstName;
document.getElementById("roleNavBar").innerHTML = "with roles: " + thisUser.roleSet;
document.getElementById("logout").setAttribute('href', '/logout');

/*Заполнение ячеек таблицы для This User*/
document.getElementById("tdIdThisUser").innerHTML = thisUser.id;
document.getElementById("tdFirstNameThisUser").innerHTML = thisUser.firstName;
document.getElementById("tdLastNameThisUser").innerHTML = thisUser.lastName;
document.getElementById("tdGenderThisUser").innerHTML = thisUser.gender;
document.getElementById("tdLoginThisUser").innerHTML = thisUser.login;
document.getElementById("tdRoleSetThisUser").innerHTML = thisUser.roleSet;


/*Функции с fetch*/
/*PATCH/POST*/
const modifyUser = async (method, url, body) => {
    const id = body.id;
    const headers = {
        "Content-Type": "application/json"
    }
    return await fetch(url, {
        method: method,
        body: JSON.stringify(body),
        headers: headers
    }).then(response => {
        if (response.ok) {
            return response.json()
        }
        return response.json().then(error => {
            const e = new Error("Ошибка в Patch запросе!");
            e.data = error;

            /*Помечаем поля с ошибками*/
            checkingFields(method, error["message"].split(":"), id);

            state = clearState(state);

            throw e;
        })
    })
}

/*DELETE*/
const deleteUser = async (deleteUrl) => {
    return await fetch(deleteUrl, {
        method: "DELETE"
    })
}

/*GET Заполнение страницы Admin*/
const loadLinkUsers = async () => {
    const response = await fetch(linkUsersUrl);
    return await response.json();
}

/*Функции*/

/*Функция высвечивает красный текст с ошибкой, над полем, в котором эта ошибка указана*/
const checkingFields = (method, arrError, id) => {
    for (let key in arrError) {
        if (method === "POST") {
            switch (arrError[key]) {
                case "firstName":
                    document.getElementById("errFirstName" + method).setAttribute("style", "color:red");
                    console.log("Пришел в errFirstName" + method);
                    break;
                case "lastName":
                    document.getElementById("errLastName" + method).setAttribute("style", "color:red");
                    break;
                case "gender":
                    document.getElementById("errGender" + method).setAttribute("style", "color:red");
                    break;
                case "login":
                    document.getElementById("errLogin" + method).setAttribute("style", "color:red");
                    break;
                case "password":
                    document.getElementById("errPassword" + method).setAttribute("style", "color:red");
                    break;
                case "roleSet":
                    document.getElementById("errRoleSet" + method).setAttribute("style", "color:red");
                    break;
                default:
                    break;
            }
        } else {

            let arrDiv = document.getElementById("modal-edit" + id).getElementsByTagName("div");
                switch (arrError[key]) {
                    case "firstName":
                        arrDiv["errFirstName" + method].setAttribute("style", "color:red");
                        break;
                    case "lastName":
                        arrDiv["errLastName" + method].setAttribute("style", "color:red");
                        break;
                    case "gender":
                        arrDiv["errGender" + method].setAttribute("style", "color:red");
                        break;
                    case "login":
                        arrDiv["errLogin" + method].setAttribute("style", "color:red");
                        break;
                    case "password":
                        arrDiv["errPassword" + method].setAttribute("style", "color:red");
                        break;
                    case "roleSet":
                        arrDiv["errRoleSet" + method].setAttribute("style", "color:red");
                        break;
                    default:
                        break;
                }

    }
}
}

/*Функция для очиститки объекта промежуточного состояния*/
const clearState = function (state) {
    for (let key in state) {
        state[key] = undefined;
    }
    return state;
}

/*Заполнение строки-шаблона из html данными user*/
let fillingRow = (user) => {
    document.getElementById("tdId").innerHTML = user.id;
    document.getElementById("tdFirstName").innerHTML = user.firstName;
    document.getElementById("tdLastName").innerHTML = user.lastName;
    document.getElementById("tdGender").innerHTML = user.gender;
    document.getElementById("tdLogin").innerHTML = user.login;
    document.getElementById("tdRoleSet").innerHTML = user.roleSet;
}

/*Настройка кнопок Edit и Delete*/
let createButton = (user) => {
    document.getElementById("buttonEdit").setAttribute("href", "#modal-edit" + user.id);
    document.getElementById("buttonEdit").addEventListener("click", () => state = clearState(state));
    document.getElementById("buttonDelete").setAttribute("href", "#modal-delete" + user.id);
    document.getElementById("tdEdit").appendChild(document.getElementById("buttonEdit"));
    document.getElementById("tdDelete").appendChild(document.getElementById("buttonDelete"));
}

/*Создание клона всей строки(со всеми ячейками и кнопками) и настройка ее Id*/
let createRow = (user) => {
    let tr = document.getElementById("rowForUser").cloneNode(true);
    tr.id = "rowForUser" + user.id;
    return tr;
}

/*Создание модального окна Edit*/
let createModalEdit = async (user, modifyUrl, row) => {
    /*Устанавливаем плейсхолдеры для полей модального окна Edit*/
    document.getElementById("id-edit").setAttribute("placeholder", user.id);
    document.getElementById("first-name-edit").setAttribute("placeholder", user.firstName);
    document.getElementById("last-name-edit").setAttribute("placeholder", user.lastName);
    document.getElementById("gender-edit").setAttribute("placeholder", user.gender);
    document.getElementById("login-edit").setAttribute("placeholder", user.login);
    document.getElementById("labelBtnEdit").setAttribute("for", "btn-edit" + user.id);

    /*Создаем клон модального окна Edit и устанавливаем ему свой Id*/
    let modalEdit = document.getElementById("modal-edit").cloneNode(true);
    modalEdit.id = "modal-edit" + user.id;

    /*Создание клона кнопки Edit*/
    let btnEdit = document.getElementById("btn-edit").cloneNode(true);
    btnEdit.id = "btn-edit" + user.id;

    /*Настройка события полей заполнения модального окна Edit*/
    setEvents(modalEdit);

    /*Настройка события кнопки Edit*/
    btnEdit.addEventListener("click", () => {
        /*Очищаем все ошибки, если они были*/
        let arrDiv = modalEdit.getElementsByTagName("div");
                arrDiv["errFirstNamePATCH"].setAttribute("style", "color:red; display:none");
                arrDiv["errLastNamePATCH"].setAttribute("style", "color:red; display:none");
                arrDiv["errGenderPATCH"].setAttribute("style", "color:red; display:none");
                arrDiv["errLoginPATCH"].setAttribute("style", "color:red; display:none");
                arrDiv["errPasswordPATCH"].setAttribute("style", "color:red; display:none");
                arrDiv["errRoleSetPATCH"].setAttribute("style", "color:red; display:none");

        state.id = user.id;
        let result = modifyUser("PATCH", modifyUrl, state);
        //------------------------------------------------------------------------------------------------------
        let checkState = true;
        for (let key in state) {
            if (state[key] === undefined) {
                checkState = false;
            }
        }
        console.log(checkState)
        if (checkState) {
            let arrState = Object.values(state);
            for (let i = 0; i < arrState.length - 1; i++) {
                row.getElementsByTagName("td")[i].innerHTML = arrState[i];
            }
        }
        checkState = true;
        state = clearState(state);
    });
    /*Добавление клона кнопки Edit в клон модального окна Edit*/
    modalEdit.appendChild(btnEdit);
    return modalEdit;
}

/*Создание модального окна Delete*/
let createModalDelete = async (user, modifyUrl, row) => {
    /*Устанавливаем плейсхолдеры для полей модального окна Delete*/
    document.getElementById("id-delete").setAttribute("placeholder", user.id);
    document.getElementById("first-name-delete").setAttribute("placeholder", user.firstName);
    document.getElementById("last-name-delete").setAttribute("placeholder", user.lastName);
    document.getElementById("gender-delete").setAttribute("placeholder", user.gender);
    document.getElementById("login-delete").setAttribute("placeholder", user.login);
    document.getElementById("labelBtnDelete").setAttribute("for", "btn-delete" + user.id);

    /*Создаем клон модального окна Delete и устанавливаем ему свой Id*/
    let modalDelete = document.getElementById("modal-delete").cloneNode(true);
    modalDelete.id = "modal-delete" + user.id;

    /*Создание клона кнопки Delete*/
    let btnDelete = document.getElementById("btn-edit").cloneNode(true);
    btnDelete.id = "btn-delete" + user.id;

    /*Настройка события кнопки Delete*/
    btnDelete.addEventListener("click", () => {
        deleteUser(modifyUrl);
        document.getElementById(row.id).remove();
        state = clearState(state);
    });

    /*Добавление клона кнопки Delete в клон модального окна Delete*/
    modalDelete.appendChild(btnDelete);
    return modalDelete;
}

/*Добавление клонов строки и модальных окон в таблицу*/
let addRowAndModal = (tr, modalEdit, modalDelete) => {
    document.getElementById("tableForLinkUsers").appendChild(tr);
    document.getElementById("tableForLinkUsers").appendChild(modalEdit);
    document.getElementById("tableForLinkUsers").appendChild(modalDelete);
}

/*Метод, который настраивает события для полей ввода*/
let setEvents = (element) => {
    /*Настройка события полей ввода для формы New*/
    element.querySelector(".inputFirstNameEdit").addEventListener("change", e => state.firstName = e.target.value);
    element.querySelector(".inputLastNameEdit").addEventListener("change", e => state.lastName = e.target.value);
    element.querySelector(".inputGenderEdit").addEventListener("change", e => state.gender = e.target.value);
    element.querySelector(".inputLoginEdit").addEventListener("change", e => state.login = e.target.value);
    element.querySelector(".inputRoleSetEdit").addEventListener("change", e => {
        let options = e.target.options;
        let roles = [];
        for (let i = 0, l = options.length; i < l; i++) {
            if (options[i].selected) {
                roles.push(options[i].text);
            }
        }
        state.roleSet = roles.join(" ");
    });
    element.querySelector(".inputPasswordEdit").addEventListener("change", e => state.password = e.target.value);
}

/*Настройка формы New*/
let formNew = async () => {
    let formNew = document.getElementById("form-new");
    setEvents(formNew);
    /*Настройка события кнопки New user*/
    document.getElementById("btnNew").addEventListener("click", async () => {
        /*Очищаем все ошибки, если они были*/
        document.getElementById("errFirstNamePOST").setAttribute("style", "color:red; display:none");
        document.getElementById("errLastNamePOST").setAttribute("style", "color:red; display:none");
        document.getElementById("errGenderPOST").setAttribute("style", "color:red; display:none");
        document.getElementById("errLoginPOST").setAttribute("style", "color:red; display:none");
        document.getElementById("errPasswordPOST").setAttribute("style", "color:red; display:none");
        document.getElementById("errRoleSetPOST").setAttribute("style", "color:red; display:none");

        /*Получаем объект для передачи в json без поля id(оно у нас устанавливается автоматически в бд)
        и передаем его в рест-запрос*/
        let newState = {};
        for (let key in state) {
            if (key.indexOf("id") === -1) {
                newState[key] = state[key];
            }
        }
        await modifyUser("POST", addUrl, newState);
        state = clearState(state);


        /*Из обновленного списка пользователей получаем двух последних user*/
        linkUsers = await loadLinkUsers();
        await createCloneRow(linkUsers[linkUsers.length - 2], linkUsers[linkUsers.length - 1]);
    });
}

/*Функция, который создает из строки первого пользователя, клон и заполняет его данными второго пользователя
* Помогает динамически добавить строку, после добавления нового user*/
let createCloneRow = async (firstUser, lastUser) => {
    let modifyUrl = "/api/users/" + lastUser.id;
    let cloneRow = document.getElementById("rowForUser" + firstUser.id).cloneNode(true);
    /*Меняем ей Id*/
    cloneRow.id = "rowForUser" + lastUser.id;

    /*Конвертируем lastUser из объекта в массив.
    Это требуется для удобного заполнения ячеек, потому,
    что мы сможем брать по одинаковому индексу и поле lastUser и нужную ячейку*/
    let arrLastUser = Object.values(lastUser);
    for (let i = 0; i < arrLastUser.length - 1; i++) {
        cloneRow.getElementsByTagName("td")[i].innerHTML = arrLastUser[i];
    }

    /*Устанавливаем правильные ссылки на кнопки Edit и Delete*/
    let arrBtn = cloneRow.getElementsByTagName("a");
    arrBtn[0].setAttribute("href", "#modal-edit" + lastUser.id);
    arrBtn[1].setAttribute("href", "#modal-delete" + lastUser.id);

    /*Создаем новые модальные окна для последнего user*/
    let modalEditForLastUser = await createModalEdit(lastUser, modifyUrl, cloneRow);
    let modalDeleteForLastUser = await createModalDelete(lastUser, modifyUrl, cloneRow);

    /*Добавляем строку и модальные окна в таблицу*/
    addRowAndModal(cloneRow, modalEditForLastUser, modalDeleteForLastUser);
}

let linkUsers = await loadLinkUsers();

/*Цикл для заполнения таблицы*/
for (let user of linkUsers) {
    let id = user.id;
    const modifyUrl = "/api/users/" + id;

    console.log("Сейчас добавляю: " + user.firstName + " " + id);

    /*Заполнение строки-шаблона из html данными user*/
    fillingRow(user);

    /*Настройка кнопок Edit и Delete*/
    createButton(user);

    /*Создаем клон всей строки(со всеми ячейками и кнопками) и присваиваем ей Id*/
    let tr = createRow(user);

    /*Создание клонов из шаблонов для модульных окон и строки + настройка параметров этих элементов*/
    let modalEdit = await createModalEdit(user, modifyUrl, tr);
    let modalDelete = await createModalDelete(user, modifyUrl, tr);

    /*Добавление клонов строки и модальных окон в таблицу*/
    addRowAndModal(tr, modalEdit, modalDelete);
}
await formNew();


/*Удаление строки-шаблона*/
document.getElementById("rowForUser").remove();




