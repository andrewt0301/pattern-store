import React, {useEffect, useReducer, useState} from 'react';
import './App.css';
import Container from 'react-bootstrap/Container';
import {Route, Routes, useNavigate, useParams} from "react-router-dom";
import {DocsCommonPage} from "../DocsCommonPage/DocsCommonPage";
import {DocById} from "../DocById/DocById";
import {DocsPrivate} from "../DocsPrivate/DocsPrivate";
import {DocDelete} from "../DocDelete/DocDelete";
import {LoginPage} from "../LoginPage/LoginPage";
import {userService} from "../Authentication/Authentication";
import {Button} from "react-bootstrap";
import {LogoutPage} from "../LogoutPage/LogoutPage";

const displayEmojiName = event => alert(event.target.id);

function showAdditional(additional) {
    const alertInformation = Object.entries(additional)
        .map(information => `${information[0]}: ${information[1]}`)
        .join('\n');
    alert(alertInformation)
}

function App() {
    const displayAction = false;
    const [show, toggle] = useReducer(state => !state, true);

    return (
        <Container>
            <div className="mb-2">
                <nav className="navbar nav-tabs navbar-expand-lg navbar-light bg-light mb-2">
                    <div className="collapse navbar-collapse" id="navbarSupportedContent">
                        <a className="navbar-brand mx-2" href="/">Pattern-store</a>
                        <ul className="navbar-nav mr-auto nav-justified">
                            <li className="nav-item">
                                <a className="nav-link" href="/">Home</a>
                            </li>
                            <li className="nav-item">
                                <a className="nav-link" href="/docs">Docs</a>
                            </li>
                        </ul>
                    </div>
                    {localStorage.getItem("user") === null
                    && <div className="nav-item justify-content-end">
                        <a className="nav-link" href="/login">Login</a>
                    </div>}
                    {localStorage.getItem("user") !== null
                    && <div className="nav-item justify-content-end">
                        <a className="nav-link" href="/logout">Logout</a>
                    </div>}
                </nav>
                <Routes>
                    <Route path="/" exact={true} element={<Home/>}/>
                    {/*<Route path="/" exact={true} element={<HomePage/>}/>*/}
                    <Route path="/login" exact={true} element={<LoginRouter/>}/>}
                    <Route path="/logout" exact={true} element={<LogoutRouter/>}/>
                    <Route path="/docs" exact={true} element={<DocsPrivate/>}/>
                    <Route path="/doc" exact={true} element={<DocById/>}/>
                    <Route path="/docs/filtered" exact={true} element={<DocsCommonPage/>}/>
                    <Route path="/doc/:docId" exact={true} element={<DocByIdRouter/>}/>
                    <Route path="/doc/:docId/delete" exact={true} element={<DocDeleteRouter/>}/>
                    <Route path="/contacts" exact={true} element={<Contacts/>}/>
                </Routes>
            </div>
        </Container>
    )
}

function DocByIdRouter() {
    let {docId} = useParams();
    return (
        <DocById docId={docId}/>
    );
}

function LogoutRouter() {
    userService.logout()
    return (
        <LogoutPage/>
    )
}

function LoginRouter() {
    const navigate = useNavigate()
    return (
        <LoginPage navigator={navigate}/>
    )
}

function DocDeleteRouter() {
    let {docId} = useParams();
    return (
        <DocDelete docId={docId}/>
    );
}

function Home() {
    return <h2>Home</h2>;
}

function Contacts() {
    return <h2>Contacts</h2>;
}

export default App;
