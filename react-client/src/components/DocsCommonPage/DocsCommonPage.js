import React, {useEffect, useState} from "react";
import {getCommonDocs} from "../../services/getCommonDocs";
import {Docs} from "../Docs/Docs";

export function DocsCommonPage() {
    const [docs, setDocs] = useState([])
    useEffect(() => {
        let mounted = true;
        getCommonDocs()
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
