using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace FavoritesTvShows.Api.Data
{
    public class ApplicationDbContext : DbContext
    {
        public DbSet<Favorite> Favorites { get; set; }

        public ApplicationDbContext(DbContextOptions<ApplicationDbContext> options) 
            : base(options)
        {

        }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.Entity<Favorite>(entity =>
            {
                entity.HasKey(a => a.Id);
                entity.ToTable("Favorites");
                entity.Property(a => a.UserId).HasMaxLength(50);
                entity.Property(a => a.TvShowId).HasMaxLength(10);
            });
        }
    }


    public class Favorite
    {
        public int Id { get; set; }
        public string  UserId { get; set; }
        public string TvShowId{ get; set; }
    }
}
