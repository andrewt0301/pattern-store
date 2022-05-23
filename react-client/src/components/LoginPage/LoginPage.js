import React from 'react';
import {userService} from "../Authentication/Authentication";

export class LoginPage extends React.Component {
    constructor(props) {
        super(props);
        userService.logout();
        this.state = {
            username: "",
            password: "",
            submitted: false,
            loading: false,
            error: ""
        };
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleChange(event) {
        const {name, value} = event.target;
        this.setState({[name]: value});
    }

    handleSubmit(event) {
        event.preventDefault();
        this.setState({submitted: true});
        const {username, password, returnUrl} = this.state;
        if (!(username && password)) {
            return;
        }
        this.setState({loading: true});
        userService.login(username, password)
            .then(
                user => {
                    this.props.navigator("/")
                    window.location.reload()
                },
                error => this.setState({error, loading: false})
            );
    }

    render() {
        const {username, password, submitted, loading, error} = this.state;
        return (
            <div className="col-md-4 col-md-offset-3">
                <h2>Login</h2>
                <form name="form" onSubmit={this.handleSubmit}>
                    <div className={"form-group" + (submitted && !username ? " has-error" : '')}>
                        <label htmlFor="username">Username</label>
                        <input type="text" className="form-control"
                               name="username"
                               value={username}
                               placeholder="username"
                               onChange={this.handleChange}/>
                        {submitted && !username &&
                        <div className="alert-danger my-1">Username is required</div>
                        }
                    </div>
                    <div className={"form-group" + (submitted && !password ? " has-error" : "")}>
                        <label htmlFor="password">Password</label>
                        <input type="password" className="form-control"
                               name="password"
                               value={password}
                               placeholder="password"
                               onChange={this.handleChange}/>
                        {submitted && !password &&
                        <div className="alert-danger my-1">Password is required</div>
                        }
                    </div>
                    <div className="form-group">
                        <button className="btn btn-primary my-2" disabled={loading}>Login</button>
                        {loading &&
                        <img
                            className="mx-3"
                            src="data:image/gif;base64,R0lGODlhEAAQAPIAAP///wAAAMLCwkJCQgAAAGJiYoKCgpKSkiH/C05FVFNDQVBFMi4wAwEAAAAh/hpDcmVhdGVkIHdpdGggYWpheGxvYWQuaW5mbwAh+QQJCgAAACwAAAAAEAAQAAADMwi63P4wyklrE2MIOggZnAdOmGYJRbExwroUmcG2LmDEwnHQLVsYOd2mBzkYDAdKa+dIAAAh+QQJCgAAACwAAAAAEAAQAAADNAi63P5OjCEgG4QMu7DmikRxQlFUYDEZIGBMRVsaqHwctXXf7WEYB4Ag1xjihkMZsiUkKhIAIfkECQoAAAAsAAAAABAAEAAAAzYIujIjK8pByJDMlFYvBoVjHA70GU7xSUJhmKtwHPAKzLO9HMaoKwJZ7Rf8AYPDDzKpZBqfvwQAIfkECQoAAAAsAAAAABAAEAAAAzMIumIlK8oyhpHsnFZfhYumCYUhDAQxRIdhHBGqRoKw0R8DYlJd8z0fMDgsGo/IpHI5TAAAIfkECQoAAAAsAAAAABAAEAAAAzIIunInK0rnZBTwGPNMgQwmdsNgXGJUlIWEuR5oWUIpz8pAEAMe6TwfwyYsGo/IpFKSAAAh+QQJCgAAACwAAAAAEAAQAAADMwi6IMKQORfjdOe82p4wGccc4CEuQradylesojEMBgsUc2G7sDX3lQGBMLAJibufbSlKAAAh+QQJCgAAACwAAAAAEAAQAAADMgi63P7wCRHZnFVdmgHu2nFwlWCI3WGc3TSWhUFGxTAUkGCbtgENBMJAEJsxgMLWzpEAACH5BAkKAAAALAAAAAAQABAAAAMyCLrc/jDKSatlQtScKdceCAjDII7HcQ4EMTCpyrCuUBjCYRgHVtqlAiB1YhiCnlsRkAAAOwAAAAAAAAAAAA=="/>
                        }
                    </div>
                    {error &&
                    <div className="alert alert-danger">{error}</div>
                    }
                </form>
            </div>
        );
    }
}
