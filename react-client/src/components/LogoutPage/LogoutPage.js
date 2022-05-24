import {Link} from "react-router-dom";
import React from "react";
import {Navigate} from "react-router";

export class LogoutPage extends React.Component {
    render() {
        return <div>
            <h5>
                You are logged out.<br/>
                In order to log in please input username and password on the login page.
            </h5>
            <Link to="/login">
                <div className="btn btn-primary m-3">Go to login page</div>
            </Link>
        </div>
    }
}
