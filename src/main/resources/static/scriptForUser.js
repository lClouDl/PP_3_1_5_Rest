/*URL*/
const thisUserUrl = 'http://localhost:8080/api/users/thisUser';

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




