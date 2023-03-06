	ORG $1000

t1 DS.W 1
x DS.W 1
t5 DS.W 1
t3 DS.W 1
y DS.W 1

START:
; Botam al bloc de codi que representa el nostre main.
	JSR main
	JMP end
;   [SKIP, null, null, main]
main:
;   [COPY, 3, null, t1]
	MOVE.W #3,t1
;   [COPY, t1, null, x]
	MOVE.W t1,x
;   [COPY, 3, null, t5]
	MOVE.W #3,t5
;   [PROD, t5, 4, t3]
	MOVE.W t5, D0
	MOVE.W #4, D1
	MULS.W D0, D1
	MOVE.W D1,t3
;   [IND_VAL, x, t3, t6]
;   [COPY, t6, null, y]
	MOVE.W t6,y
;   [RTN, null, null, null]
	RTS
PRINTINT:
	MOVEM.W D0-D1,-(A7)
	CLR.L D1
	MOVE.W 8(A7),D1
	EXT.L D1
	MOVE #3,D0
	TRAP #15
	MOVEM.W (A7)+,D0-D1
	RTS

GETINT:
	MOVEM.W D0-D1,-(A7)
	MOVE.W #4,D0
	CLR.L D1
	TRAP #15
	MOVE.W D1,D7
	MOVEM.W (A7)+,D0-D1
	RTS

PRINTNL:
	MOVE.W D0,-(A7)
	MOVE.W A1,-(A7)
	MOVE.W #13,D0
	MOVE.L #11,D0
	MOVE.W #$00FF,D1
	TRAP #15
	ADD.W #1,D1
	AND.W #$00FF,D1
	TRAP #15
	MOVE.W (A7)+,A1
	MOVE.W (A7)+,D0
	RTS
end:
	SIMHALT
	END START
