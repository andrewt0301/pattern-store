import React from "react";

export function Home() {
    return (
        <div>
            <h3>Home page</h3>
            <div className="p">You are welcome, {localStorage.getItem("username")}!</div>
        </div>
    )
}
