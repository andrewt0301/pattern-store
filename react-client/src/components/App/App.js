import React from 'react';
import './App.css';
import Container from 'react-bootstrap/Container';
import {Route, Routes, useNavigate, useParams} from "react-router-dom";
import {DocsCommonPage} from "../DocsCommonPage/DocsCommonPage";
import {DocById} from "../DocById/DocById";
import {DocsPrivate} from "../DocsPrivate/DocsPrivate";
import {DocDelete} from "../DocDelete/DocDelete";
import {LoginPage} from "../LoginPage/LoginPage";
import {userService} from "../Authentication/Authentication";
import {LogoutPage} from "../LogoutPage/LogoutPage";
import {Home} from "../HomePage/HomePage";
import {Navigate} from "react-router";
import {ProtectedRoute} from "../helpers/ProtectedRoute";
import {DocFeedbackAdd} from "../DocFeedbackAdd/DocFeedbackAdd";
import {DocFeedbackView} from "../DocFeedbackView/DocFeedbackView";

const displayEmojiName = event => alert(event.target.id);

function showAdditional(additional) {
    const alertInformation = Object.entries(additional)
        .map(information => `${information[0]}: ${information[1]}`)
        .join('\n');
    alert(alertInformation)
}

function App() {
    let userStrg = localStorage.getItem("user")
    return (
        <Container>
            <div className="mb-2">
                <nav className="navbar nav-tabs navbar-expand-lg navbar-light bg-light mb-2 ">
                    <div className="collapse navbar-collapse" id="navbarSupportedContent">
                        <a className="navbar-brand mx-2" href="/">Pattern-store</a>
                        <ul className="navbar-nav mr-auto nav-justified">
                            <li className="nav-item">
                                <a className="nav-link" href="/">Home</a>
                            </li>
                            <li className="nav-item">
                                <a className="nav-link" href="/docs/filtered">Common docs</a>
                            </li>
                            <li className="nav-item">
                                <a className="nav-link" href="/docs">Private docs</a>
                            </li>
                        </ul>
                    </div>
                    {userStrg === null
                    && <div className="nav-item justify-content-end">
                        <a className="nav-link" href="/login">Login</a>
                    </div>}
                    {userStrg !== null
                    && <div className="nav-item justify-content-end">
                        <a className="nav-link" href="/logout">Logout</a>
                    </div>}
                </nav>
                <Routes>
                    <Route path="/" exact={true} element={<Home/>}/>
                    <Route path="/login" exact={true} element={<LoginRouter/>}/>}
                    <Route path="/logout" exact={true} element={<LogoutRouter/>}/>
                    <Route path="/docs" exact={true} element={
                        <ProtectedRoute user={userStrg}>
                            <DocsPrivate/>
                        </ProtectedRoute>
                    }/>
                    <Route path="/doc" exact={true} element={<DocById/>}/>
                    <Route path="/docs/filtered" exact={true} element={<DocsCommonPage/>}/>
                    <Route path="/doc/:docId" exact={true} element={<DocByIdRouter/>}/>
                    <Route path="/doc/:docId/feedback/add" exact={true} element={<DocFeedbackAddRouter/>}/>
                    <Route path="/doc/:docId/feedback/view" exact={true} element={<DocFeedbackViewRouter/>}/>
                    <Route path="/doc/:docId/delete" exact={true} element={
                        <ProtectedRoute user={userStrg}>
                            <DocDeleteRouter/>
                        </ProtectedRoute>
                    }/>
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

function DocFeedbackAddRouter() {
    let {docId} = useParams();
    return (
        <DocFeedbackAdd docId={docId}/>
    );
}

function DocFeedbackViewRouter() {
    let {docId} = useParams();
    return (
        <DocFeedbackView docId={docId}/>
    );
}

function Contacts() {
    return <h2>Contacts</h2>;
}

export default App;
