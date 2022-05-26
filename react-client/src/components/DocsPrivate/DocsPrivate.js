import React, {useEffect, useState} from "react";
import {Docs} from "../Docs/Docs";
import {getDocsByUser} from "../../services/getDocsByUser";

export function DocsPrivate() {
    const [docs, setDocs] = useState([])
    useEffect(() => {
        let mounted = true;
        getDocsByUser()
            .then(items => {
                if (mounted) {
                    setDocs(items)
                }
            })
        return () => mounted = false;
    }, [])
    return (
        <Docs title="Common documents" docs={docs}/>
    );
}
