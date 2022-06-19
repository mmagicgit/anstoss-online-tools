package service

type Player struct {
	Name          string
	Team          string
	OffensiveWon  int
	OffensiveLost int
	DefensiveWon  int
	DefensiveLost int
}

type OneOnOne struct {
	Minute        int
	AttackingTeam string
	Attacker      string
	Defender      string
	AttackerWins  bool
}
