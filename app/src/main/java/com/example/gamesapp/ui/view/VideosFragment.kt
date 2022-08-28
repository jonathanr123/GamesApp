package com.example.gamesapp.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gamesapp.R
import com.example.gamesapp.databinding.FragmentVideosBinding
import com.example.gamesapp.domain.model.MediaObject
import com.example.gamesapp.ui.view.adapters.MediaRecyclerAdapter
import com.example.gamesapp.ui.viewmodel.VideosViewModel
import com.example.gamesapp.utils.ExoPlayerRecyclerView
import com.example.gamesapp.utils.visible
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList

@AndroidEntryPoint
class VideosFragment : Fragment() {

    // Properties
    private lateinit var binding: FragmentVideosBinding
    private val videosViewModel: VideosViewModel by activityViewModels()

    private lateinit var rvVideos: ExoPlayerRecyclerView
    private val adapterVideos = MediaRecyclerAdapter()
    private var mediaObjectList = ArrayList<MediaObject>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentVideosBinding.inflate(layoutInflater, container, false)

        // Show bottom navigation view
        val activity = requireActivity() as MainActivity
        activity.findViewById<BottomNavigationView>(R.id.navigationView).visible()

        // Set Up Toolbar
        activity.supportActionBar?.show()
        activity.supportActionBar?.title = "New and Popular"
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(false)

        // Prepare demo content
        prepareVideoList()

        // Init RecyclerView
        setUpRecyclerViewVideos()

        return binding.root
    }

    private fun setUpRecyclerViewVideos() {
        rvVideos = binding.exoPlayerRecyclerView
        rvVideos.layoutManager = LinearLayoutManager(this.requireContext())

        // Set data object
        rvVideos.setMediaObjects(mediaObjectList)
        adapterVideos.submitList(mediaObjectList)

        // Set Adapter
        rvVideos.adapter = adapterVideos
        rvVideos.smoothScrollToPosition(1)
    }

    override fun onDestroy() {
        rvVideos.releasePlayer()
        super.onDestroy()
    }

    override fun onPause() {
        rvVideos.onPausePlayer()
        super.onPause()
    }

    private fun prepareVideoList() {
        mediaObjectList = arrayListOf(
            MediaObject(
                id = 1,
                title = "Far Cry 6",
                description = "Welcome to Yara, a tropical paradise frozen in time. As the dictator of Yara, " +
                        "Anton Castillo is intent on restoring his nation back to its former glory by any means, " +
                        "with his son, Diego, following in his bloody footsteps. Their oppressive rule has ignited " +
                        "a revolution.",
                genres = "Action, Shooter, Adventure",
                coverUrl = "https://media.rawg.io/media/crop/600/400/games/5dd/5dd4d2dd986d2826800bc37fff64aa4f.jpg",
                url = "https://media.rawg.io/media/stories-640/1af/1af02ce32304129a25f9772895c07e19.mp4"
            ),
            MediaObject(
                id = 2,
                title = "Cyberpunk 2077",
                description = "The player controls V from the first person view, with the third-person view used for" +
                        " cutscenes only. The protagonist can travel across the city on feet or using various vehicles," +
                        " in a manner some observers compared to GTA series. There are many options for the character" +
                        " customization, including three character classes, and a variety of augmentations V can install" +
                        " to enhance his or her abilities.",
                genres = "Action, Adventure, RPG",
                coverUrl = "https://media.rawg.io/media/crop/600/400/games/26d/26d4437715bee60138dab4a7c8c59c92.jpg",
                url = "https://media.rawg.io/media/stories-640/f78/f789c8011d52e0ffac76b11a88fabee7.mp4"
            ),
            MediaObject(
                id = 3,
                title = "Marvel's Spider-Man",
                description = "The game introduces Spider-Man as an already experienced superhero. By the time the" +
                        " game begins, Peter has captured the infamous Kingpin as well as several other supervillains." +
                        " Now, a gang that goes by the name of Demons poses a new danger to New York. Meanwhile, Peter" +
                        " is working for the scientist Otto Octavius, who didn't yet become Dr. Octopus, on their" +
                        " science project. Throughout the game, Spidey has to deal with multiple enemies, such as" +
                        " Norman Osbourne, Electro, Vulture, Rhino and Kingpin, among others.",
                genres = "Action, Adventure",
                coverUrl = "https://www.muycomputer.com/wp-content/uploads/2021/08/Spider-Man.jpg",
                url = "https://media.rawg.io/media/stories/d24/d24935c22314743aaa063a7507ab6e01.mp4"
            ),
            MediaObject(
                id = 4,
                title = "Killzone Shadow Fall",
                description = "The sixth game of the Killzone series exclusive to Sony PlayStation consoles. The" +
                        " story begins several years after the construction of “The Wall”. The year is 2370, " +
                        "planetary colonization sparks a conflict between two factions on the remote planet. Michael" +
                        " Kellan and his son Lucas attempt to sneak to it. Like it predecessors Shadow Fall features" +
                        " faction packed the first-person shooter in the sci-fi game world.",
                genres = "Shooter",
                coverUrl = "https://media.rawg.io/media/resize/1280/-/games/dfa/dfa0906773ebb8a50d15548ac5b8ee5e.jpg",
                url = "https://media.rawg.io/media/stories/425/4251f3c4ee147184393f26c7464542bb.mp4"
            ),
            MediaObject(
                id = 5,
                title = "Humankind",
                description = "HUMANKIND™ is Amplitude Studios' magnum opus, a Historical Strategy game where YOU" +
                        " will re-write the entire narrative of humankind – a convergence of culture, history, and" +
                        " values that allows you to create a civilization that is as unique as you are. HOW FAR WILL" +
                        " YOU PUSH HUMANKIND?",
                genres = "Strategy",
                coverUrl = "https://i.ytimg.com/vi/-TzK4lWEp_I/maxresdefault.jpg",
                url = "https://media.rawg.io/media/stories/67b/67ba44abc7ce2840944d5c5003f5c9e1.mp4"
            ),
            MediaObject(
                id = 6,
                title = "Nickelodeon All-Star Brawl",
                description = "Nickelodeon All-Star Brawl, is a new platform fighting game featuring fan-favorite" +
                        " Nickelodeon characters. Its roster includes familiar faces from SpongeBob SquarePants," +
                        " Rugrats, the Teenage Mutant Ninja Turtles, Danny Phantom, Hey Arnold!, Invader Zim, The" +
                        " Loud House, Aaahh!!! Real Monsters, and more. Nickelodeon's new fighting game will feature" +
                        " 20 themed levels, such as Jellyfish Fields from SpongeBob SquarePants and TMNT's Technodrome.",
                genres = "Action, Fighting",
                coverUrl = "https://media.rawg.io/media/resize/1280/-/games/a53/a53575214f12dadb9f6d7dca9b24cda4.jpg",
                url = "https://media.rawg.io/media/stories/56b/56b47d165d9476c711789007121e3179.mp4"
            ),
            MediaObject(
                id = 7,
                title = "PlayerUnknown’s Battlegrounds",
                description = "PLAYERUNKNOWN’S BATTLEGROUNDS is battle-royale shooter made only for the multiplayer" +
                        " experience. Players should do their best to become the last one alive. Matches are held" +
                        " on 3 main maps of a large size (Erangel, Miramar, and Sanhok). Each match has 100" +
                        " participants fighting for themselves. The battle begins with players making the leap out" +
                        " of the plane to spread out across the map and to gather all possible equipment required" +
                        " for survival.",
                genres = "Action, Shooter, Massively Multiplayer",
                coverUrl = "https://media.rawg.io/media/resize/1280/-/games/1bd/1bd2657b81eb0c99338120ad444b24ff.jpg",
                url = "https://media.rawg.io/media/stories/699/699c2e2acf55dc9b35c904a867849171.mp4"
            ),
            MediaObject(
                id = 8,
                title = "Fortnite Battle Royale",
                description = "Fortnite Battle Royale is the completely free 100-player PvP mode in Fortnite. " +
                        "One giant map. A battle bus. Fortnite building skills and destructible environments " +
                        "combined with intense PvP combat. The last one standing wins. Download now for FREE and" +
                        " jump into the action. This download also gives you a path to purchase the Save the World" +
                        " co-op PvE campaign during Fortnite’s Early Access season, or instant access if you received" +
                        " a Friend invite.",
                genres = "Action, Shooter",
                coverUrl = "https://media.rawg.io/media/resize/1280/-/games/dcb/dcbb67f371a9a28ea38ffd73ee0f53f3.jpg",
                url = "https://media.rawg.io/media/stories/65f/65ff9214af64ca0c89abac55d80ed7ab.mp4"
            ),
            MediaObject(
                id = 9,
                title = "The Sims 4",
                description = "Enjoy the power to create and control people in a virtual world where there are" +
                        " no rules. Express your creativity as you customize your Sims’ appearances and personalities," +
                        " and build them the perfect homes. Develop your Sims’ relationships, pursue careers, and" +
                        " explore vibrant new worlds.",
                genres = "Strategy, Simulation, Casual",
                coverUrl = "https://media.rawg.io/media/resize/1280/-/games/e44/e445335e611b4ccf03af71fffcbd30a4.jpg",
                url = "https://media.rawg.io/media/stories/b44/b44d6ff46251bb24c8ef25a32eb58a71.mp4"
            ),
            MediaObject(
                id = 10,
                title = "Sniper Elite 3",
                description = "This prequel to Sniper Elite V2 is set three years before the events of the previous" +
                        " game, in Northern Africa during the World War II. The player assumes the role of US special" +
                        " forces operative Karl Fairburne. His task is to eliminate the Nazi German general Franz" +
                        " Vahlen before he completes his project of a doomsday weapon. The locations and background" +
                        " events are inspired by the real historical places and battles.",
                genres = "Action, Shooter, Adventure",
                coverUrl = "https://media.rawg.io/media/resize/1280/-/games/fe6/fe68940746007261e34da4d9cce81423.jpg",
                url = "https://media.rawg.io/media/stories/58b/58beaac70db20f4ef207f51596397859.mp4"
            )
        )
    }
}
