class Khaldi_Leila_Player extends Player {
    int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
        
        // First rounds = always cooperate:
        if (n < 5) return 0;

		// Calculate defection rates:
		int opp1Defects = 0, opp2Defects = 0;
		for (int i = 0; i < n; i++) {
			if (oppHistory1[i] == 1) opp1Defects++;
			if (oppHistory2[i] == 1) opp2Defects++;
		}
		double opp1Rate = (double) opp1Defects / n;
		double opp2Rate = (double) opp2Defects / n;

		// Recent defection rate (last 10 games)
		int lookback = Math.min(n, 10);
		int opp1Recent = 0, opp2Recent = 0;
		for (int i = n - lookback; i < n; i++) {
			if (oppHistory1[i] == 1) opp1Recent++;
			if (oppHistory2[i] == 1) opp2Recent++;
		}
		double opp1RecentRate = (double) opp1Recent / lookback;
		double opp2RecentRate = (double) opp2Recent / lookback;

		// Detect player types
		boolean opp1Nasty = (opp1Rate > 0.9);
		boolean opp2Nasty = (opp2Rate > 0.9);
		boolean opp1Nice = (opp1Rate < 0.05);
		boolean opp2Nice = (opp2Rate < 0.05);

		// Cooperation streak
		int streak = 0;
		for (int i = n-1; i >= 0; i--) {
			if (oppHistory1[i] == 0 && oppHistory2[i] == 0)
				streak++;
			else
				break;
		}

		// If both are nasty, defect
		if (opp1Nasty && opp2Nasty) return 1;
		// If there has been a cooperation streak, safe to cooperate
		if (streak >= 5) return 0;
		// If both are nice, cooperate
		if (opp1Nice && opp2Nice) return 0;
		// One nasty and one nice, defect to gain from nice one
		if ((opp1Nasty && opp2Nice) || (opp1Nice && opp2Nasty)) return 1;
			
		// Use recent rates for the main decision
		// Both recently cooperative
		if (opp1RecentRate < 0.3 && opp2RecentRate < 0.3) return 0;

		// If both have been nasty recently
		if (opp1RecentRate > 0.7 && opp2RecentRate > 0.7) return 1;

		// If at least one of them have been nasty recently
		if (opp1RecentRate > 0.5 || opp2RecentRate > 0.5) return 1;

		// Default cooperate
		return 0;
    }