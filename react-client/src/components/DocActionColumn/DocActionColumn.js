import {Link} from "react-router-dom";
import React from "react";

export function DocActionColumn() {
    return (
        <div className="list-group">
            <Link to="/docs">
                <button type="button" className="list-group-item list-group-item-action">
                    Filter documents
                </button>
            </Link>
            <Link to="/docs/filtered">
                <div className="list-group-item list-group-item-action">
                    Filter common documents
                </div>
            </Link>
            <Link to="/doc">
                <button type="button" className="list-group-item list-group-item-action">
                    Get document by id
                </button>
            </Link>
        </div>
    )
}