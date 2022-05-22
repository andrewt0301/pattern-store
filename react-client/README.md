## Run project
At first, common and service modules should be built.
Module `common` can be built by following the installation
instructions in `commmon/README.md`. Then it is necessary to
follow instructions from `service/README.md` for **building**
`service` module and **running** service.   
After that the next commands should be run to build 
`react-client` module:
```shell
npm run build
npm install -g serve
serve -s build/
```

## Available Scripts

In the project directory, you can run:

```shell
npm start
```

Runs the app in the development mode.
Open [http://localhost:3000](http://localhost:3000) to view
it in the browser. The page will reload when you make changes.
You may also see any lint errors in the console.

```shell
npm run build
```

Builds the app for production to the `build` folder.
It correctly bundles React in production mode and optimizes the build for the best performance.
The build is minified and the filenames include the hashes.
Your app is ready to be deployed!

See the section about [deployment](https://facebook.github.io/create-react-app/docs/deployment) for more information.
