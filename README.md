# Learning clojure with emacs

This is my workflow for learning both emacs and clojure

- All editing and repl work down in emacs
- Learn snippets and execute them in repl, dumping what i find interesting into clojure.clj
- Any wordy notes about clojure in here
- A little every day is better than a lot every 2 weeks

## Writing clojure in emacs

https://github.com/clojure-emacs/cider is the emacs package for enabling this. 

### Running the repl

`M-x cider-jack-in`

At the end of a line do `c-x c-e` to evaluate it in the repl

`C-c M-n` sets the repl's namespace to whatever is defined in the open file

`C-c C-k` compiles the clojure file in the repl

When you get some kind of stacktrace window you can get rid of it with `q`. If you want it back it will be in a buffer called `cider-error`

### Paredit

A minor mode which closes the lisp parenthesis. There are key bindings to navigate through them

Given `(+ 1 2 3 4)` you can put the point (cursor) before 2 for example and type`m-(` which will put paranthesis around 2

You can then "slup" an argument into the current paranthesis with `c-right arrow`. Doing left arrow will do the opposite

https://github.com/georgek/paredit-cheatsheet/blob/master/paredit-cheatsheet.pdf?raw=true
