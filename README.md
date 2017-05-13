DPA simulator
=============

Solution for the third lab at "Introduction to Theoretical Computer Science" course - Deterministic pushdown automaton which
accepts input sequences with acceptable state.

Example
------

Input file consists of minimum 5 rows in this specific order:

1. line - input sequences separated with `|`, symbols of every sequence are separated with `,`
2. line - states separated with `,`
3. line - alphabet symbols separated with `,`
4. line - stack symbols separated with `,`
5. line - acceptable states separated with `,`
6. line - starting state
7. line - starting stack symbol
8. line and all other lines - transition function in format `currentState,alphabetSymbol,stackSymbol->newState,stackSymbols`.
Empty collection of stack symbols is marked with `$`.

Input

    0|0,2,0|1,2,0
    q1,q2,q3
    0,1,2
    J,N,K
    q3
    q1
    K
    q1,0,K->q1,NK
    q1,1,K->q1,JK
    q1,0,N->q1,NN
    q1,1,N->q1,JN
    q1,0,J->q1,NJ
    q1,1,J->q1,JJ
    q1,2,K->q2,K
    q1,2,N->q2,N
    q1,2,J->q2,J
    q2,0,N->q2,$
    q2,1,J->q2,$
    q2,$,K->q3,$

will give output

    q1#K|q1#NK|0
    q1#K|q1#NK|q2#NK|q2#K|q3#$|1
    q1#K|q1#JK|q2#JK|fail|0

Output format is `state#stack` and on the end `0` for acceptable state and `1` for unacceptable state.

If input sequence is not completely processed and there is no possible transition for current state, symbol and stack
output should be `fail`.
