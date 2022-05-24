import {deleteDocById} from "../../services/deleteDocById";
import {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";

function showResult(text) {
    alert(text)
}

export function DocDelete(docId) {
    const [success, setSuccess] = useState(false);
    const navigate = useNavigate()
    useEffect(() => {
        let mounted = true;
        docId && deleteDocById(docId.docId)
            .then(resp => {
                let redirectByDocId = false
                if (resp.status !== 200) {
                    if (resp.status === 401 || resp.status === 403) {
                        if (mounted) {
                            setSuccess(false)
                            redirectByDocId = true
                            showResult("You don't have permission to delete this document")
                        }
                    } else {
                        const error = (resp && resp.error) || resp.status;
                        if (mounted) {
                            setSuccess(false)
                            redirectByDocId = true
                            showResult({error})
                        }
                    }
                } else {
                    if (mounted) {
                        setSuccess(true)
                        redirectByDocId = false
                        showResult("Doc was successfully deleted")
                    }
                }
                if (redirectByDocId) {
                    {docId && navigate("/doc/" + docId.docId)}
                } else {
                    navigate("/docs/filtered")
                }
            })
        return () => mounted = false;
    }, [])
}
