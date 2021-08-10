function TaskQueue(handler) {
    let queue = [];

    function run() {
        let queueCallback = function () {
            queue.shift();
            // when the handler says it's finished (i.e. runs the callback)
            // we check for more tasks in the queue and if there are any we run again
            if (queue.length > 0) {
                run();
            }
        }

        // give the first item in the queue & the callback to the handler
        handler(queue[0].task, queue[0].argsArray, queue[0].taskCallback, queueCallback);
    }

    // push the task, its callback and arguments.
    // if the queue was empty before the task was pushed
    // we run the task
    this.append = function (task, argsArray, taskCallback) {
        queue.push({ task: task, argsArray: argsArray, taskCallback: taskCallback });
        if (queue.length === 1) {
            run();
        }
    }
}

// fetch handler
const FETCH_QUEUE = new TaskQueue(function (task, argsArray, taskCallback, queueCallback) {
    argsArray.push(function () {
        taskCallback.apply(null, arguments);
        queueCallback();
    });

    task.apply(null, argsArray);
});
