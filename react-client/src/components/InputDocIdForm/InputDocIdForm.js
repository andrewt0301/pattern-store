import React, {useState} from "react";
import {useNavigate} from "react-router-dom";

export function InputDocIdForm() {
    const [id, setId] = useState('');
    const [alert, setAlert] = useState(false);
    const navigate = useNavigate();
    const validate = event => {
        console.log(id)
        if(/^[0-9a-f]{24}$/.test(id)) {
            setAlert(false);
            navigate("/doc/" + id);
        }
        event.preventDefault();
        setAlert(true);
    };
    return (
        <div>
            <div className="form-group w-25">
                <label htmlFor="exampleInputDocId">Document id</label>
                <input type="text" className="form-control mb-2 mr-sm-2"
                       id="exampleInputDocId"
                       aria-describedby="docIdHelp"
                       placeholder="Document id"
                       onChange={event => setId(event.target.value) }
                />
                {alert && <div className="text-danger">Input should contain 24 HEX-characters</div>}
                <small id="docIdHelp" className="form-text text-muted">Doc id (24 hex characters)</small>
            </div>
            <div className="btn btn-primary my-2" onClick={validate}>Search</div>
        </div>
    )
}
