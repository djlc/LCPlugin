package com.github.djlc;

import org.bukkit.Location;
import org.bukkit.Material;

public class Cube {

	Cube(Location l, int n, Material m) {
		for (int i=0; i<n; i++) {
			for (int j=0; j<n; j++) {
				for (int k=0; k<n; k++) {
					l.getBlock().setType(m);
					l.setX(l.getX() + 1.0);
				}
				l.setX(l.getX() - n);
				l.setY(l.getY() + 1.0);
			}
			l.setY(l.getY() - n);
			l.setZ(l.getZ() + 1.0);
		}
	}
}
