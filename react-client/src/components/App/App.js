import React, {lazy, useReducer, useState} from 'react';
import './App.css';
import Container from 'react-bootstrap/Container';
import {Route, Routes} from "react-router-dom";
import {DocPage} from "../DocPage/DocPage";

// const RiverInformation = lazy(() => import(/* webpackChunkName: "RiverInformation" */ '../RiverInformation/RiverInformation'));

const displayEmojiName = event => alert(event.target.id);

function showAdditional(additional) {
    const alertInformation = Object.entries(additional)
        .map(information => `${information[0]}: ${information[1]}`)
        .join('\n');
    alert(alertInformation)
}

function App() {
    const greeting = "greeting";
    const displayAction = false;
    const [river, setRiver] = useState('nile');
    const [show, toggle] = useReducer(state => !state, true);

    return (
        <Container>
            <div className="mb-2">
                <nav className="navbar nav-tabs navbar-expand-lg navbar-light bg-light">
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
                    <Route path="/" element={<Home/>}/>
                    <Route path="/docs" element={<DocPage/>}/>
                    <Route path="/contacts" element={<Contacts/>}/>
                </Routes>
            </div>
        </Container>
    )
}

function Home() {
    return <h2>Home</h2>;
}

function Docs() {
    return <h2>Here will be docs</h2>;
}

function Contacts() {
    return <h2>Contacts</h2>;
}

export default App;