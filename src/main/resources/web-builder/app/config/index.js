const req = require.context('./', true, /^(?!\.\/index\.js$).+\.js$/);
const configurations = [];

req.keys().forEach(file => {
    const config = req(file);
    configurations.push(config.default ? config.default : config);
});

export default configurations;
