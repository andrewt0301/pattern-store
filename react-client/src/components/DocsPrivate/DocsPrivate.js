import {DocActionColumn} from "../DocActionColumn/DocActionColumn";
import React from "react";

export function DocsPrivate() {
    return (
        <div className="row">
            <div className="col-sm-8">
                <h2>Here will be private documents with filter</h2>
            </div>
            <div className="col-sm-3">
                <DocActionColumn/>
            </div>
        </div>
    )
}
