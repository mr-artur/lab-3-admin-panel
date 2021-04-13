const languageButtons = document.getElementsByClassName("btn-language")

const changeLanguage = e => {
    const { target: { value } } = e
    const { location : { pathname, search } } = window;
    console.log("" + location + " " + pathname + " " + value)
    window.location.replace(buildNewLocation(pathname, value) + search)
}

const buildNewLocation = (pathname, value) => {
    return buildLocationWithLang(pathname, value)
}

const buildLocationWithLang = (pathname, value) => {
    if ((value == "en" && pathname.includes("/en"))
        || (value != "en" && !pathname.includes("/en"))) {
        return pathname
    }
    return value == "en"
        ? "/en" + pathname
        : pathname.replace("/en", "")
}

if (languageButtons) {
    for (let i = 0; i < languageButtons.length; i++) {
        languageButtons[i].addEventListener("click", changeLanguage)
    }
}
