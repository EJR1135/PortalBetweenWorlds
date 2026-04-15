package io.github.EJR1135.portalbetweenworlds.gen.dim;

import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.SandBlock;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.screen.LoadingDisplay;
import net.minecraft.util.math.noise.OctavePerlinNoiseSampler;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSource;
import net.minecraft.world.gen.carver.Carver;
import net.minecraft.world.gen.carver.CaveCarver;
import net.minecraft.world.gen.feature.CactusPatchFeature;
import net.minecraft.world.gen.feature.ClayOreFeature;
import net.minecraft.world.gen.feature.DeadBushPatchFeature;
import net.minecraft.world.gen.feature.DungeonFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.GrassPatchFeature;
import net.minecraft.world.gen.feature.LakeFeature;
import net.minecraft.world.gen.feature.OreFeature;
import net.minecraft.world.gen.feature.PlantPatchFeature;
import net.minecraft.world.gen.feature.PumpkinPatchFeature;
import net.minecraft.world.gen.feature.SpringFeature;
import net.minecraft.world.gen.feature.SugarCanePatchFeature;
import net.modificationstation.stationapi.impl.world.CaveGenBaseImpl;
import net.modificationstation.stationapi.impl.world.chunk.FlattenedChunk;

public class otherWorldChunkGenerator implements ChunkSource {
	private Random random;
	private OctavePerlinNoiseSampler minLimitPerlinNoise;
	private OctavePerlinNoiseSampler maxLimitPerlinNoise;
	private OctavePerlinNoiseSampler perlinNoise1;
	private OctavePerlinNoiseSampler perlinNoise2;
	private OctavePerlinNoiseSampler perlinNoise3;
	public OctavePerlinNoiseSampler floatingIslandScale;
	public OctavePerlinNoiseSampler floatingIslandNoise;
	public OctavePerlinNoiseSampler forestNoise;
	private World world;
	private double[] heightMap;
	private double[] sandBuffer = new double[256];
	private double[] gravelBuffer = new double[256];
	private double[] depthBuffer = new double[256];
	private Carver cave;
	private Biome[] biomes;
	double[] perlinNoiseBuffer;
	double[] minLimitPerlinNoiseBuffer;
	double[] maxLimitPerlinNoiseBuffer;
	double[] scaleNoiseBuffer;
	double[] depthNoiseBuffer;
	int[][] waterDepths = new int[32][32];
	private double[] temperatures;

	public otherWorldChunkGenerator(World world, long seed) {
		this.world = world;
		this.random = new Random(seed);
		this.minLimitPerlinNoise = new OctavePerlinNoiseSampler(this.random, 16);
		this.maxLimitPerlinNoise = new OctavePerlinNoiseSampler(this.random, 16);
		this.perlinNoise1 = new OctavePerlinNoiseSampler(this.random, 8);
		this.perlinNoise2 = new OctavePerlinNoiseSampler(this.random, 4);
		this.perlinNoise3 = new OctavePerlinNoiseSampler(this.random, 4);
		this.floatingIslandScale = new OctavePerlinNoiseSampler(this.random, 10);
		this.floatingIslandNoise = new OctavePerlinNoiseSampler(this.random, 16);
		this.forestNoise = new OctavePerlinNoiseSampler(this.random, 8);

		this.cave = new CaveCarver();
		if (this.cave instanceof CaveGenBaseImpl caveGen) {
    		caveGen.stationapi_setWorld(this.world);
		}
	}

	public void buildTerrain(int chunkX, int chunkZ, byte[] blocks, Biome[] biomes, double[] temperatures) {
		byte var6 = 4;
		byte var7 = 64;
		int var8 = var6 + 1;
		byte var9 = 17;
		int var10 = var6 + 1;
		this.heightMap = this.generateHeightMap(this.heightMap, chunkX * var6, 0, chunkZ * var6, var8, var9, var10);

		for (int var11 = 0; var11 < var6; var11++) {
			for (int var12 = 0; var12 < var6; var12++) {
				for (int var13 = 0; var13 < 16; var13++) {
					double var14 = 0.125;
					double var16 = this.heightMap[((var11 + 0) * var10 + var12 + 0) * var9 + var13 + 0];
					double var18 = this.heightMap[((var11 + 0) * var10 + var12 + 1) * var9 + var13 + 0];
					double var20 = this.heightMap[((var11 + 1) * var10 + var12 + 0) * var9 + var13 + 0];
					double var22 = this.heightMap[((var11 + 1) * var10 + var12 + 1) * var9 + var13 + 0];
					double var24 = (this.heightMap[((var11 + 0) * var10 + var12 + 0) * var9 + var13 + 1] - var16) * var14;
					double var26 = (this.heightMap[((var11 + 0) * var10 + var12 + 1) * var9 + var13 + 1] - var18) * var14;
					double var28 = (this.heightMap[((var11 + 1) * var10 + var12 + 0) * var9 + var13 + 1] - var20) * var14;
					double var30 = (this.heightMap[((var11 + 1) * var10 + var12 + 1) * var9 + var13 + 1] - var22) * var14;

					for (int var32 = 0; var32 < 8; var32++) {
						double var33 = 0.25;
						double var35 = var16;
						double var37 = var18;
						double var39 = (var20 - var16) * var33;
						double var41 = (var22 - var18) * var33;

						for (int var43 = 0; var43 < 4; var43++) {
							int var44 = var43 + var11 * 4 << 11 | 0 + var12 * 4 << 7 | var13 * 8 + var32;
							short var45 = 128;
							double var46 = 0.25;
							double var48 = var35;
							double var50 = (var37 - var35) * var46;

							for (int var52 = 0; var52 < 4; var52++) {
								double var53 = temperatures[(var11 * 4 + var43) * 16 + var12 * 4 + var52];
								int var55 = 0;
								if (var13 * 8 + var32 < var7) {
									if (var53 < 0.5 && var13 * 8 + var32 >= var7 - 1) {
										var55 = Block.ICE.id;
									} else {
										var55 = Block.WATER.id;
									}
								}

								if (var48 > 0.0) {
									var55 = Block.STONE.id;
								}

								blocks[var44] = (byte)var55;
								var44 += var45;
								var48 += var50;
							}

							var35 += var39;
							var37 += var41;
						}

						var16 += var24;
						var18 += var26;
						var20 += var28;
						var22 += var30;
					}
				}
			}
		}
	}

	public void buildSurfaces(int chunkX, int chunkZ, byte[] blocks, Biome[] biomes) {
		byte var5 = 64;
		double var6 = 0.03125;
		this.sandBuffer = this.perlinNoise2.create(this.sandBuffer, chunkX * 16, chunkZ * 16, 0.0, 16, 16, 1, var6, var6, 1.0);
		this.gravelBuffer = this.perlinNoise2.create(this.gravelBuffer, chunkX * 16, 109.0134, chunkZ * 16, 16, 1, 16, var6, 1.0, var6);
		this.depthBuffer = this.perlinNoise3.create(this.depthBuffer, chunkX * 16, chunkZ * 16, 0.0, 16, 16, 1, var6 * 2.0, var6 * 2.0, var6 * 2.0);

		for (int var8 = 0; var8 < 16; var8++) {
			for (int var9 = 0; var9 < 16; var9++) {
				Biome var10 = biomes[var8 + var9 * 16];
				boolean var11 = this.sandBuffer[var8 + var9 * 16] + this.random.nextDouble() * 0.2 > 0.0;
				boolean var12 = this.gravelBuffer[var8 + var9 * 16] + this.random.nextDouble() * 0.2 > 3.0;
				int var13 = (int)(this.depthBuffer[var8 + var9 * 16] / 3.0 + 3.0 + this.random.nextDouble() * 0.25);
				int var14 = -1;
				byte var15 = var10.topBlockId;
				byte var16 = var10.soilBlockId;

				for (int var17 = 127; var17 >= 0; var17--) {
					int var18 = (var9 * 16 + var8) * 128 + var17;
					if (var17 <= 0 + this.random.nextInt(5)) {
						blocks[var18] = (byte)Block.BEDROCK.id;
					} else {
						byte var19 = blocks[var18];
						if (var19 == 0) {
							var14 = -1;
						} else if (var19 == Block.STONE.id) {
							if (var14 == -1) {
								if (var13 <= 0) {
									var15 = 0;
									var16 = (byte)Block.STONE.id;
								} else if (var17 >= var5 - 4 && var17 <= var5 + 1) {
									var15 = var10.topBlockId;
									var16 = var10.soilBlockId;
									if (var12) {
										var15 = 0;
									}

									if (var12) {
										var16 = (byte)Block.GRAVEL.id;
									}

									if (var11) {
										var15 = (byte)Block.SAND.id;
									}

									if (var11) {
										var16 = (byte)Block.SAND.id;
									}
								}

								if (var17 < var5 && var15 == 0) {
									var15 = (byte)Block.WATER.id;
								}

								var14 = var13;
								if (var17 >= var5 - 1) {
									blocks[var18] = var15;
								} else {
									blocks[var18] = var16;
								}
							} else if (var14 > 0) {
								var14--;
								blocks[var18] = var16;
								if (var14 == 0 && var16 == Block.SAND.id) {
									var14 = this.random.nextInt(4);
									var16 = (byte)Block.SANDSTONE.id;
								}
							}
						}
					}
				}
			}
		}
	}

	@Override
	public FlattenedChunk loadChunk(int chunkX, int chunkZ) {
		return this.getChunk(chunkX, chunkZ);
	}

	@Override
	public FlattenedChunk getChunk(int chunkX, int chunkZ) {
		this.random.setSeed(chunkX * 341873128712L + chunkZ * 132897987541L);
		byte[] var3 = new byte[32768];
		FlattenedChunk chunk = new FlattenedChunk(this.world, chunkX, chunkZ);
		this.biomes = this.world.method_1781().getBiomesInArea(this.biomes, chunkX * 16, chunkZ * 16, 16, 16);
		double[] temp = this.world.method_1781().temperatureMap;
		this.buildTerrain(chunkX, chunkZ, var3, this.biomes, temp);
		this.buildSurfaces(chunkX, chunkZ, var3, this.biomes);
		this.cave.carve(this, this.world, chunkX, chunkZ, var3);
		chunk.fromLegacy(var3);
		chunk.populateHeightMap();
		return chunk;
	}

	private double[] generateHeightMap(double[] heightMap, int x, int y, int z, int sizeX, int sizeY, int sizeZ) {
		if (heightMap == null) {
			heightMap = new double[sizeX * sizeY * sizeZ];
		}

		double var8 = 684.412;
		double var10 = 684.412;
		double[] var12 = this.world.method_1781().temperatureMap;
		double[] var13 = this.world.method_1781().downfallMap;
		this.scaleNoiseBuffer = this.floatingIslandScale.create(this.scaleNoiseBuffer, x, z, sizeX, sizeZ, 1.121, 1.121, 0.5);
		this.depthNoiseBuffer = this.floatingIslandNoise.create(this.depthNoiseBuffer, x, z, sizeX, sizeZ, 200.0, 200.0, 0.5);
		this.perlinNoiseBuffer = this.perlinNoise1.create(this.perlinNoiseBuffer, x, y, z, sizeX, sizeY, sizeZ, var8 / 80.0, var10 / 160.0, var8 / 80.0);
		this.minLimitPerlinNoiseBuffer = this.minLimitPerlinNoise.create(this.minLimitPerlinNoiseBuffer, x, y, z, sizeX, sizeY, sizeZ, var8, var10, var8);
		this.maxLimitPerlinNoiseBuffer = this.maxLimitPerlinNoise.create(this.maxLimitPerlinNoiseBuffer, x, y, z, sizeX, sizeY, sizeZ, var8, var10, var8);
		int var14 = 0;
		int var15 = 0;
		int var16 = 16 / sizeX;

		for (int var17 = 0; var17 < sizeX; var17++) {
			int var18 = var17 * var16 + var16 / 2;

			for (int var19 = 0; var19 < sizeZ; var19++) {
				int var20 = var19 * var16 + var16 / 2;
				double var21 = var12[var18 * 16 + var20];
				double var23 = var13[var18 * 16 + var20] * var21;
				double var25 = 1.0 - var23;
				var25 *= var25;
				var25 *= var25;
				var25 = 1.0 - var25;
				double var27 = (this.scaleNoiseBuffer[var15] + 256.0) / 512.0;
				var27 *= var25;
				if (var27 > 1.0) {
					var27 = 1.0;
				}

				double var29 = this.depthNoiseBuffer[var15] / 8000.0;
				if (var29 < 0.0) {
					var29 = -var29 * 0.3;
				}

				var29 = var29 * 3.0 - 2.0;
				if (var29 < 0.0) {
					var29 /= 2.0;
					if (var29 < -1.0) {
						var29 = -1.0;
					}

					var29 /= 1.4;
					var29 /= 2.0;
					var27 = 0.0;
				} else {
					if (var29 > 1.0) {
						var29 = 1.0;
					}

					var29 /= 8.0;
				}

				if (var27 < 0.0) {
					var27 = 0.0;
				}

				var27 += 0.5;
				var29 = var29 * sizeY / 16.0;
				double var31 = sizeY / 2.0 + var29 * 4.0;
				var15++;

				for (int var33 = 0; var33 < sizeY; var33++) {
					double var34 = 0.0;
					double var36 = (var33 - var31) * 12.0 / var27;
					if (var36 < 0.0) {
						var36 *= 4.0;
					}

					double var38 = this.minLimitPerlinNoiseBuffer[var14] / 512.0;
					double var40 = this.maxLimitPerlinNoiseBuffer[var14] / 512.0;
					double var42 = (this.perlinNoiseBuffer[var14] / 10.0 + 1.0) / 2.0;
					if (var42 < 0.0) {
						var34 = var38;
					} else if (var42 > 1.0) {
						var34 = var40;
					} else {
						var34 = var38 + (var40 - var38) * var42;
					}

					var34 -= var36;
					if (var33 > sizeY - 4) {
						double var44 = (var33 - (sizeY - 4)) / 3.0F;
						var34 = var34 * (1.0 - var44) + -10.0 * var44;
					}

					heightMap[var14] = var34;
					var14++;
				}
			}
		}

		return heightMap;
	}

	@Override
	public boolean isChunkLoaded(int x, int z) {
		return true;
	}

	@Override
	public void decorate(ChunkSource source, int x, int z) {
		SandBlock.fallInstantly = true;
		int var4 = x * 16;
		int var5 = z * 16;
		Biome var6 = this.world.method_1781().getBiome(var4 + 16, var5 + 16);
		this.random.setSeed(this.world.getSeed());
		long var7 = this.random.nextLong() / 2L * 2L + 1L;
		long var9 = this.random.nextLong() / 2L * 2L + 1L;
		this.random.setSeed(x * var7 + z * var9 ^ this.world.getSeed());
		double var11 = 0.25;
		if (this.random.nextInt(4) == 0) {
			int var13 = var4 + this.random.nextInt(16) + 8;
			int var14 = this.random.nextInt(128);
			int var15 = var5 + this.random.nextInt(16) + 8;
			new LakeFeature(Block.WATER.id).generate(this.world, this.random, var13, var14, var15);
		}

		if (this.random.nextInt(8) == 0) {
			int var26 = var4 + this.random.nextInt(16) + 8;
			int var38 = this.random.nextInt(this.random.nextInt(120) + 8);
			int var50 = var5 + this.random.nextInt(16) + 8;
			if (var38 < 64 || this.random.nextInt(10) == 0) {
				new LakeFeature(Block.LAVA.id).generate(this.world, this.random, var26, var38, var50);
			}
		}

		for (int var27 = 0; var27 < 8; var27++) {
			int var39 = var4 + this.random.nextInt(16) + 8;
			int var51 = this.random.nextInt(128);
			int var16 = var5 + this.random.nextInt(16) + 8;
			new DungeonFeature().generate(this.world, this.random, var39, var51, var16);
		}

		for (int var28 = 0; var28 < 10; var28++) {
			int var40 = var4 + this.random.nextInt(16);
			int var52 = this.random.nextInt(128);
			int var63 = var5 + this.random.nextInt(16);
			new ClayOreFeature(32).generate(this.world, this.random, var40, var52, var63);
		}

		for (int var29 = 0; var29 < 20; var29++) {
			int var41 = var4 + this.random.nextInt(16);
			int var53 = this.random.nextInt(128);
			int var64 = var5 + this.random.nextInt(16);
			new OreFeature(Block.DIRT.id, 32).generate(this.world, this.random, var41, var53, var64);
		}

		for (int var30 = 0; var30 < 10; var30++) {
			int var42 = var4 + this.random.nextInt(16);
			int var54 = this.random.nextInt(128);
			int var65 = var5 + this.random.nextInt(16);
			new OreFeature(Block.GRAVEL.id, 32).generate(this.world, this.random, var42, var54, var65);
		}

		for (int var31 = 0; var31 < 20; var31++) {
			int var43 = var4 + this.random.nextInt(16);
			int var55 = this.random.nextInt(128);
			int var66 = var5 + this.random.nextInt(16);
			new OreFeature(Block.COAL_ORE.id, 16).generate(this.world, this.random, var43, var55, var66);
		}

		for (int var32 = 0; var32 < 20; var32++) {
			int var44 = var4 + this.random.nextInt(16);
			int var56 = this.random.nextInt(64);
			int var67 = var5 + this.random.nextInt(16);
			new OreFeature(Block.IRON_ORE.id, 8).generate(this.world, this.random, var44, var56, var67);
		}

		for (int var33 = 0; var33 < 2; var33++) {
			int var45 = var4 + this.random.nextInt(16);
			int var57 = this.random.nextInt(32);
			int var68 = var5 + this.random.nextInt(16);
			new OreFeature(Block.GOLD_ORE.id, 8).generate(this.world, this.random, var45, var57, var68);
		}

		for (int var34 = 0; var34 < 8; var34++) {
			int var46 = var4 + this.random.nextInt(16);
			int var58 = this.random.nextInt(16);
			int var69 = var5 + this.random.nextInt(16);
			new OreFeature(Block.REDSTONE_ORE.id, 7).generate(this.world, this.random, var46, var58, var69);
		}

		for (int var35 = 0; var35 < 1; var35++) {
			int var47 = var4 + this.random.nextInt(16);
			int var59 = this.random.nextInt(16);
			int var70 = var5 + this.random.nextInt(16);
			new OreFeature(Block.DIAMOND_ORE.id, 7).generate(this.world, this.random, var47, var59, var70);
		}

		for (int var36 = 0; var36 < 1; var36++) {
			int var48 = var4 + this.random.nextInt(16);
			int var60 = this.random.nextInt(16) + this.random.nextInt(16);
			int var71 = var5 + this.random.nextInt(16);
			new OreFeature(Block.LAPIS_ORE.id, 6).generate(this.world, this.random, var48, var60, var71);
		}

		var11 = 0.5;
		int var37 = (int)((this.forestNoise.sample(var4 * var11, var5 * var11) / 8.0 + this.random.nextDouble() * 4.0 + 4.0) / 3.0);
		int var49 = 0;
		if (this.random.nextInt(10) == 0) {
			var49++;
		}

		if (var6 == Biome.FOREST) {
			var49 += var37 + 5;
		}

		if (var6 == Biome.RAINFOREST) {
			var49 += var37 + 5;
		}

		if (var6 == Biome.SEASONAL_FOREST) {
			var49 += var37 + 2;
		}

		if (var6 == Biome.TAIGA) {
			var49 += var37 + 5;
		}

		if (var6 == Biome.DESERT) {
			var49 -= 20;
		}

		if (var6 == Biome.TUNDRA) {
			var49 -= 20;
		}

		if (var6 == Biome.PLAINS) {
			var49 -= 20;
		}

		for (int var61 = 0; var61 < var49; var61++) {
			int var72 = var4 + this.random.nextInt(16) + 8;
			int var17 = var5 + this.random.nextInt(16) + 8;
			Feature var18 = var6.getRandomTreeFeature(this.random);
			var18.prepare(1.0, 1.0, 1.0);
			var18.generate(this.world, this.random, var72, this.world.getTopY(var72, var17), var17);
		}

		byte var62 = 0;
		if (var6 == Biome.FOREST) {
			var62 = 2;
		}

		if (var6 == Biome.SEASONAL_FOREST) {
			var62 = 4;
		}

		if (var6 == Biome.TAIGA) {
			var62 = 2;
		}

		if (var6 == Biome.PLAINS) {
			var62 = 3;
		}

		for (int var73 = 0; var73 < var62; var73++) {
			int var76 = var4 + this.random.nextInt(16) + 8;
			int var85 = this.random.nextInt(128);
			int var19 = var5 + this.random.nextInt(16) + 8;
			new PlantPatchFeature(Block.DANDELION.id).generate(this.world, this.random, var76, var85, var19);
		}

		byte var74 = 0;
		if (var6 == Biome.FOREST) {
			var74 = 2;
		}

		if (var6 == Biome.RAINFOREST) {
			var74 = 10;
		}

		if (var6 == Biome.SEASONAL_FOREST) {
			var74 = 2;
		}

		if (var6 == Biome.TAIGA) {
			var74 = 1;
		}

		if (var6 == Biome.PLAINS) {
			var74 = 10;
		}

		for (int var77 = 0; var77 < var74; var77++) {
			byte var86 = 1;
			if (var6 == Biome.RAINFOREST && this.random.nextInt(3) != 0) {
				var86 = 2;
			}

			int var97 = var4 + this.random.nextInt(16) + 8;
			int var20 = this.random.nextInt(128);
			int var21 = var5 + this.random.nextInt(16) + 8;
			new GrassPatchFeature(Block.GRASS.id, var86).generate(this.world, this.random, var97, var20, var21);
		}

		var74 = 0;
		if (var6 == Biome.DESERT) {
			var74 = 2;
		}

		for (int var78 = 0; var78 < var74; var78++) {
			int var87 = var4 + this.random.nextInt(16) + 8;
			int var98 = this.random.nextInt(128);
			int var108 = var5 + this.random.nextInt(16) + 8;
			new DeadBushPatchFeature(Block.DEAD_BUSH.id).generate(this.world, this.random, var87, var98, var108);
		}

		if (this.random.nextInt(2) == 0) {
			int var79 = var4 + this.random.nextInt(16) + 8;
			int var88 = this.random.nextInt(128);
			int var99 = var5 + this.random.nextInt(16) + 8;
			new PlantPatchFeature(Block.ROSE.id).generate(this.world, this.random, var79, var88, var99);
		}

		if (this.random.nextInt(4) == 0) {
			int var80 = var4 + this.random.nextInt(16) + 8;
			int var89 = this.random.nextInt(128);
			int var100 = var5 + this.random.nextInt(16) + 8;
			new PlantPatchFeature(Block.BROWN_MUSHROOM.id).generate(this.world, this.random, var80, var89, var100);
		}

		if (this.random.nextInt(8) == 0) {
			int var81 = var4 + this.random.nextInt(16) + 8;
			int var90 = this.random.nextInt(128);
			int var101 = var5 + this.random.nextInt(16) + 8;
			new PlantPatchFeature(Block.RED_MUSHROOM.id).generate(this.world, this.random, var81, var90, var101);
		}

		for (int var82 = 0; var82 < 10; var82++) {
			int var91 = var4 + this.random.nextInt(16) + 8;
			int var102 = this.random.nextInt(128);
			int var109 = var5 + this.random.nextInt(16) + 8;
			new SugarCanePatchFeature().generate(this.world, this.random, var91, var102, var109);
		}

		if (this.random.nextInt(32) == 0) {
			int var83 = var4 + this.random.nextInt(16) + 8;
			int var92 = this.random.nextInt(128);
			int var103 = var5 + this.random.nextInt(16) + 8;
			new PumpkinPatchFeature().generate(this.world, this.random, var83, var92, var103);
		}

		byte var84 = 0;
		if (var6 == Biome.DESERT) {
			var84 += 10;
		}

		for (int var93 = 0; var93 < var84; var93++) {
			int var104 = var4 + this.random.nextInt(16) + 8;
			int var110 = this.random.nextInt(128);
			int var114 = var5 + this.random.nextInt(16) + 8;
			new CactusPatchFeature().generate(this.world, this.random, var104, var110, var114);
		}

		for (int var94 = 0; var94 < 50; var94++) {
			int var105 = var4 + this.random.nextInt(16) + 8;
			int var111 = this.random.nextInt(this.random.nextInt(120) + 8);
			int var115 = var5 + this.random.nextInt(16) + 8;
			new SpringFeature(Block.FLOWING_WATER.id).generate(this.world, this.random, var105, var111, var115);
		}

		for (int var95 = 0; var95 < 20; var95++) {
			int var106 = var4 + this.random.nextInt(16) + 8;
			int var112 = this.random.nextInt(this.random.nextInt(this.random.nextInt(112) + 8) + 8);
			int var116 = var5 + this.random.nextInt(16) + 8;
			new SpringFeature(Block.FLOWING_LAVA.id).generate(this.world, this.random, var106, var112, var116);
		}

		this.temperatures = this.world.method_1781().create(this.temperatures, var4 + 8, var5 + 8, 16, 16);

		for (int var96 = var4 + 8; var96 < var4 + 8 + 16; var96++) {
			for (int var107 = var5 + 8; var107 < var5 + 8 + 16; var107++) {
				int var113 = var96 - (var4 + 8);
				int var117 = var107 - (var5 + 8);
				int var22 = this.world.getTopSolidBlockY(var96, var107);
				double var23 = this.temperatures[var113 * 16 + var117] - (var22 - 64) / 64.0 * 0.3;
				if (var23 < 0.5
					&& var22 > 0
					&& var22 < 128
					&& this.world.isAir(var96, var22, var107)
					&& this.world.getMaterial(var96, var22 - 1, var107).blocksMovement()
					&& this.world.getMaterial(var96, var22 - 1, var107) != Material.ICE) {
					this.world.setBlock(var96, var22, var107, Block.SNOW.id);
				}
			}
		}

		SandBlock.fallInstantly = false;
	}

	@Override
	public boolean save(boolean saveEntities, LoadingDisplay display) {
		return true;
	}

	@Override
	public boolean tick() {
		return false;
	}

	@Override
	public boolean canSave() {
		return true;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public String getDebugInfo() {
		return "RandomLevelSource";
	}
}
