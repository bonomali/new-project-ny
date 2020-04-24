const history = process.env.NODE_ENV === 'test' ?
 require("history").createMemoryHistory() : require("history").createBrowserHistory()

export default history