import {Navigate} from "react-router";
import React from "react";

export const ProtectedRoute = ({user, children}) => {
    if (!user) {
        return <Navigate to="/login" replace/>;
    }
    return children;
};
