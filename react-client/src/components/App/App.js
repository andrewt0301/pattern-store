import React, {useReducer} from 'react';
import './App.css';
import Container from 'react-bootstrap/Container';
import {Route, Routes, useParams} from "react-router-dom";
import {DocsCommonPage} from "../DocsCommonPage/DocsCommonPage";
import {DocById} from "../DocById/DocById";
import {DocsPrivate} from "../DocsPrivate/DocsPrivate";
import {DocDelete} from "../DocDelete/DocDelete";
import {HomePage} from "../HomePage/HomePage";
import {LoginPage} from "../LoginPage/LoginPage";

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
                </nav>
                <Routes>
                    <Route path="/" exact={true} element={<Home/>}/>
                    {/*<Route path="/" exact={true} element={<HomePage/>}/>*/}
                    {/*<Route path="/login" exact={true} element={<LoginPage/>}/>*/}
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
